package com.larke.gateway.service;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.larke.gateway.dao.FileDao;
import com.larke.gateway.exception.FileException;
import com.larke.gateway.model.File;
import com.larke.gateway.model.User;
import com.larke.gateway.repository.FileRepository;
import com.larke.gateway.repository.UserRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.util.StringUtils;

@Service("fileService")
@ComponentScan(basePackages = "com.larke.gateway")
public class FileServiceImpl implements FileService {

	static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

	@Autowired
	FileRepository fileRepository;

	@Autowired
	FileDao fileDao;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	FileService fileService;

	@Value("${upload.path}")
	private String uploadPath;
	

	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(Paths.get(uploadPath));
		} catch (IOException e) {
			throw new FileException("Could not create upload folder!");
		}	
	}
	
	@Override
	public Resource downloadFile(String filename) {
		try {
			Path file = Paths.get(uploadPath).resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new FileException("Error: " + e.getMessage());
		}
	}

	@Override
	public File store(MultipartFile file) {
			File fileDB = new File();
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/files/")
					.toUriString();
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileExtension = FilenameUtils.getExtension(fileName);
			fileDB.setName(fileName);
			fileDB.setType(fileExtension);
			try {
				fileDB.setFilecontent(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileDB.setUrl(fileDownloadUri+file.getOriginalFilename());
			fileDB.setSize(file.getSize());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByEmail(auth.getName());
			fileDB.setOwner(user.getId());
			Path localPath = Paths.get(uploadPath);
			try {
				Files.copy(file.getInputStream(), localPath.resolve(file.getOriginalFilename()));
				String localDownloadUri = localPath.toUri().toString();
				fileDB.setUrl(localDownloadUri);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return fileRepository.save(fileDB);
	}

	@Override
	public Stream<File> getAllFiles() {
		return fileRepository.findAll().stream();
	}

	@Override
	public List<File> listAllFilesRepo() {
		return fileRepository.findAll();
	}
	
	@Override
	public Optional<File> getImageById(String id) {
		return fileRepository.findById(id);
	}
	
	@Override
	public void deleteFile(String id) {
		fileRepository.deleteById(id);
	}

	@Override
	public Stream<File> getLocalFile(String id) {
		return fileRepository.findById(id).stream();
	}
	
	@Override
	public File getFile(String id) {
		return fileRepository.findById(id).get();
	}

	@Override
	public File viewFile(String fileName) {
		return fileDao.findByFileName(fileName);
	}

	@Override
	public void deleteAFile(File file) {
		fileRepository.delete(file);
	}

	@Override
	public List<File> listAllFilesDAO() {
		return fileDao.listAllFilesDAO();
	}

	@Override
	public File findByFileName(String fileName) {
		return fileDao.findByFileName(fileName);

	}

}
