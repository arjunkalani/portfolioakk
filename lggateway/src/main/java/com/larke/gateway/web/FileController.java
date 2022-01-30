package com.larke.gateway.web;

import com.larke.gateway.exception.FileNotFoundException;
import com.larke.gateway.model.File;
import com.larke.gateway.model.User;
import com.larke.gateway.payload.FileUploadResponse;
import com.larke.gateway.repository.FileRepository;
import com.larke.gateway.repository.UserRepository;
import com.larke.gateway.service.FileService;
import com.larke.gateway.service.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@ComponentScan(basePackages = "com.larke.gateway")
public class FileController {

	static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
	private static final String REDIRECT = "redirect:/listFiles";
	private static final String TRUEIMAGES = "imagesAreTrue";
	private static final String LISTFILES = "listFiles";

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

	@GetMapping("/uploadFiles")
	public ModelAndView uploadFiles() {
		ModelAndView model = new ModelAndView();
		model.setViewName("dashboard/user/file/uploadFiles");
		return model;
	}

	@GetMapping("/singleUploadFile")
	public ModelAndView getSingleFileUpload() {
		return new ModelAndView("dashboard/user/file/singleUploadFile");
	}

	@PostMapping("/singleUploadFile")
	public String postSingleUploadFile(@RequestParam("file") MultipartFile file) {
		fileService.store(file);
		return REDIRECT;
	}

	@RequestMapping("/multipleUploadFiles")
	public ModelAndView getMultipleUploadFiles() {
		return new ModelAndView("dashboard/user/file/multipleUploadFiles");
	}

	@PostMapping("/multipleUploadFiles")
	public ModelAndView uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		ModelAndView model = new ModelAndView();
		model.setViewName("dashboard/user/file/fileView");
		List<FileUploadResponse> uploadFiles = Arrays.asList(files).stream().map(file -> uploadFile(file))
				.collect(Collectors.toList());
		model.addObject("uploadFiles", uploadFiles);
		model.setViewName(REDIRECT);
		return model;
	}

	@PostMapping("/uploadFile")
	public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			File fileName = fileService.store(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(fileName.getName()).toUriString();
			return new FileUploadResponse(fileName.getName(), fileDownloadUri, file.getContentType(), file.getSize());
		} else {
			return null;
		}
	}

	@GetMapping("/listFiles")
	public ModelAndView listFiles() {
		ModelAndView model = new ModelAndView();
		List<File> listFiles = fileService.listAllFilesRepo();
		model.addObject(LISTFILES, listFiles);
		model.setViewName("dashboard/user/file/listFiles");
		model.addObject(TRUEIMAGES, Boolean.FALSE);
		return model;
	}

	@GetMapping("/fileView/{id}")
	@ResponseBody
	public ModelAndView viewFile(@PathVariable("id") String id, HttpServletResponse response) {
		ModelAndView model = new ModelAndView(LISTFILES);
		Optional<File> fileDB = fileService.getImageById(id);
		if       (fileDB.get().getType().equalsIgnoreCase("jpeg") || fileDB.get().getType().equalsIgnoreCase("jpg") ||
				   fileDB.get().getType().equalsIgnoreCase("png") || fileDB.get().getType().equalsIgnoreCase("gif")) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			model.addObject(TRUEIMAGES, Boolean.TRUE);
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
		} else {
			model.addObject(TRUEIMAGES, Boolean.FALSE);
			model.addObject("fileView", fileDB);
			User user = userService.getUserById(fileDB.get().getOwner());
			model.addObject("user", user.getFullName());
			model.setViewName("dashboard/user/file/fileView");
		}
		return model;
	}

	@GetMapping("/fileDownload/{id}")
	public ModelAndView downloadAFile(@PathVariable("id") String id) {
		ModelAndView model = new ModelAndView(LISTFILES);
		File dbFile = fileService.getFile(id);
		model.addObject("fileDownload", dbFile);
		User user = userService.getUserById(dbFile.getOwner());
		model.addObject("user", user.getFullName());
		model.setViewName("dashboard/user/file/fileDownload");
		return model;
	}

	@PostMapping("/fileDownload")
	public ResponseEntity<File> downloadLocalFile(String id) {
		File dbFile = fileService.getFile(id);
		String filename = dbFile.getName();
		String link = dbFile.getUrl() + "/" + filename;
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getName() + "\"")
				.body(dbFile);
	}

	@GetMapping("/fileDelete/{id}")
	public ModelAndView getDeleteFile(@PathVariable("id") String id) {
		ModelAndView model = new ModelAndView(LISTFILES);
		File dbFile = fileService.getFile(id);
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
	
	@ModelAttribute("roles")
	private Collection<? extends GrantedAuthority> getRoles(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities();
	}

}
