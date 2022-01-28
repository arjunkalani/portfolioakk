package com.larke.gateway.web;


import com.larke.gateway.exception.FileNotFoundException;


import com.larke.gateway.message.ResponseMessage;

import java.nio.file.Files;
import com.larke.gateway.model.File;
import com.larke.gateway.model.UploadFileResponse;
import com.larke.gateway.model.User;
import com.larke.gateway.payload.FileUploadResponse;
import com.larke.gateway.payload.UploadResponseMessage;
import com.larke.gateway.repository.FileRepository;
import com.larke.gateway.repository.UserRepository;
import com.larke.gateway.service.FileService;
import com.larke.gateway.service.UserService;
import com.larke.gateway.util.FileUploadUtil;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Controller
@ComponentScan(basePackages = "com.larke.gateway")
public class FileController {
		
		static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
		private static final String REDIRECT = "redirect:/listFiles";
		

		@Value("${upload.path}")
		private String uploadPath;
		
		@Autowired
		FileService fileService;

		@Autowired
		UserService userService;

		@Autowired
		FileRepository fileRepository;

		@Autowired
		UserRepository userRepository;
		
	    @Autowired
	    public FileController(FileService fileService) {
	        this.fileService = fileService;
	    }
		
		@GetMapping(path = "/documentManagement")
		public String documentManagement(Model model) {
			return "dashboard/user/file/documentManagement";
		}

		@RequestMapping(value = { "/uploadFiles" }, method = RequestMethod.GET)
		public ModelAndView uploadFiles() {
			ModelAndView model = new ModelAndView();
			model.setViewName("dashboard/user/file/uploadFiles");
			return model;
		}
		
		@RequestMapping(value = { "/singleUploadFile" }, method = RequestMethod.GET)
		public ModelAndView getSingleFileUpload() {
			return new ModelAndView("dashboard/user/file/singleUploadFile");
		}

	    
	    @PostMapping("/singleUploadFile")
	    public String postSingleUploadFile(@RequestParam("file") MultipartFile file) {
	            fileService.store(file);
	            return "redirect:userDashboard";
	    }     
	    

	    @RequestMapping("/multipleUploadFiles")
		public ModelAndView getMultipleUploadFiles() {
			return new ModelAndView("dashboard/user/file/multipleUploadFiles");
		}
	    
	    
	    @PostMapping("/multipleUploadFiles")
	    public  ModelAndView uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
	    	ModelAndView model = new ModelAndView();
	    	model.setViewName("dashboard/user/file/fileView");
	    	List<FileUploadResponse> uploadFiles = Arrays.asList(files)
		                .stream()
		                .map(file -> uploadFile(file))
		                .collect(Collectors.toList());
	    	model.addObject("uploadFiles", uploadFiles);
	    	model.setViewName("redirect:userDashboard");
	        return model;
	    }   
	    
	    @PostMapping("/uploadFile")
	    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) {
	    	if (!file.isEmpty()) {
		    	File fileName = fileService.store(file);
		        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		                .path("/downloadFile/")
		                .path(fileName.getName())
		                .toUriString();
		        return new FileUploadResponse(fileName.getName(), fileDownloadUri,
		                file.getContentType(), file.getSize());
	    	}else {
	    		return null;
	    	}
	    }
	    
	    
	    @GetMapping("/listFiles") 
	    public ModelAndView listFiles() {
	    	ModelAndView model = new ModelAndView();
	    	List<File> listFiles= fileService.listAllFilesRepo();
			model.addObject("listFiles", listFiles);
			model.setViewName("dashboard/user/file/listFiles");
			model.addObject("imagesAreTrue", Boolean.FALSE);
	    	LOGGER.info("REPORTING FROM /listFiles: "+ "imagesAreTrue");
	        return model;
	    }  
	    
	    
	    @GetMapping("/fileView/{id}")
	    @ResponseBody
	    public ModelAndView viewFile(@PathVariable("id") String id, HttpServletResponse response) {
		      ModelAndView model = new ModelAndView("listFiles");
		      Optional<File> fileDB = fileService.getImageById(id);
			  LOGGER.info("REPORTING FROM before if: "+ fileDB.get().getType()+"imagesAreTrue");
		      if (fileDB.get().getType().equals("jpeg") || fileDB.get().getType().equals("jpg") ||
		    	  fileDB.get().getType().equals("png")  || fileDB.get().getType().equals("gif")) {
			      response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
				  model.addObject("imagesAreTrue", Boolean.TRUE);
			      LOGGER.info("REPORTING FROM inside if condition is TRUE: ");
			      try {
					response.getOutputStream().write(fileDB.get().getFilecontent());
			      } catch (IOException e) {
					e.printStackTrace();
			      }
			      try {
					response.getOutputStream().close();
			      } catch (IOException e) {
					e.printStackTrace();
			      }
		      }else{
		    	  model.addObject("imagesAreTrue", Boolean.FALSE);
			      model.addObject("fileView",  fileDB);
			      User user = userService.getUserById(fileDB.get().getOwner());
			      model.addObject("user", user.getFullName());
				  model.setViewName("dashboard/user/file/fileView");
			      LOGGER.info("REPORTING FROM inside else condition is TRUE: ");
		      }
		      LOGGER.info("REPORTING FROM before return: "+ "imagesAreTrue");
			  return model;
	    }
	    
		@GetMapping("/fileDownload/{id}")
		public ModelAndView downloadAFile(@PathVariable("id") String id) {
			 	ModelAndView model = new ModelAndView("listFiles");
			 	File dbFile = fileService.getFile(id);
			 	LOGGER.info("REPORTING FROM get fileDownload/{id}: " + id);
			 	model.addObject("fileDownload", dbFile);
			    User user = userService.getUserById(dbFile.getOwner());
			    model.addObject("user", user.getFullName());
			 	model.setViewName("dashboard/user/file/fileDownload");
			 	return model;
		}
	    
		@PostMapping("/fileDownload")
		public ResponseEntity<File> downloadLocalFile(String id) {
		 		File dbFile = fileService.getFile(id);
		 		String filename = dbFile.getName().toString();
		 		String link = dbFile.getUrl()+"/"+filename;
			 	LOGGER.info("REPORTING FROM /fileDownload: "+ filename +link);
		 		return ResponseEntity.ok()
    				.header(HttpHeaders.CONTENT_DISPOSITION, 
    						"attachment; filename=\"" + dbFile.getName() + "\"")
    				.body(dbFile);
		 	}

		@GetMapping("/fileDelete/{id}")
		public ModelAndView getDeleteFile(@PathVariable("id") String id) {
			 	ModelAndView model = new ModelAndView("listFiles");
			 	File dbFile = fileService.getFile(id);
			 	LOGGER.info("REPORTING FROM get /fileDelete/{id}: "+ id);
			 	model.addObject("fileDelete", dbFile);
			    User user = userService.getUserById(dbFile.getOwner());
			    model.addObject("user", user.getFullName());
			 	model.setViewName("dashboard/user/file/fileDelete");
			 	return model;
		}
		
		@PostMapping("/fileDelete")
		public String deleteFile(String id) {
		 	File dbFile = fileService.getFile(id);
		 	String fileName;
		 	fileName = dbFile.getName();
		 	LOGGER.info("REPORTING FROM post /fileDelete: "+ fileName);
			Path file = Paths.get(uploadPath).resolve(fileName);
			Boolean isDelete = Boolean.FALSE;
	        try {
	            isDelete = FileSystemUtils.deleteRecursively(file);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 	fileService.deleteFile(id);
			return REDIRECT;
			
		}   	


		@ExceptionHandler(FileNotFoundException.class)
		public ResponseEntity<?> handleStorageFileNotFound(FileNotFoundException exc) {
			return ResponseEntity.notFound().build();
		}	

		
		@ModelAttribute("userName")
		private String getUserName(Model model) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByEmail(auth.getName());
			return user.getFirstname() + " " + user.getLastname() + "!" + " (" + user.getEmail() + ") ";
		}
		
		@ModelAttribute("userId")
		private long getUserId(Model model) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByEmail(auth.getName());
			return user.getId();
		}

	}

