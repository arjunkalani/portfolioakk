package com.larke.gateway.model;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.ComponentScan;

@Entity
@Table(name = "file")
@ComponentScan(basePackages = "com.larke.gateway")
public class File {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	private String type;

	@Lob
	private byte[] filecontent;
	
	private String url;
	
	private Long size;

	@Column(name = "user_id")
	private long owner;
	
    @Column(nullable = true, length = 64)
    private String photos;
    
    @Column(name = "document_format")
    private String documentFormat;
    
    @Column(name = "upload_dir")
    private String uploadDir;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(byte[] filecontent) {
		this.filecontent = filecontent;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}
	
	


	public String getDocumentFormat() {
		return documentFormat;
	}

	public void setDocumentFormat(String documentFormat) {
		this.documentFormat = documentFormat;
	}

	public File() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	
	

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public File(String name, String type, byte[] filecontent) {
		super();
		this.name = name;
		this.type = type;
		this.filecontent = filecontent;
	}

	public File(String id, String name, String type, byte[] filecontent, String url, long owner) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.filecontent = filecontent;
		this.url = url;
		this.owner = owner;
	}
	
	


	public File(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public File(String id, String name, String type, String url, Long size, long owner) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.url = url;
		this.size = size;
		this.owner = owner;
	}

	public File(String name, String url, String type, byte[] filecontent) {
		super();
		this.name = name;
		this.url = url;
		this.type = type;
		this.filecontent = filecontent;
	}

	public File(String id, String name, String type, byte[] filecontent, String url, Long size, long owner) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.filecontent = filecontent;
		this.url = url;
		this.size = size;
		this.owner = owner;
	}

	public File(String id, String name, String type, byte[] filecontent, String url, Long size, long owner,
			String photos, String documentFormat, String uploadDir) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.filecontent = filecontent;
		this.url = url;
		this.size = size;
		this.owner = owner;
		this.photos = photos;
		this.documentFormat = documentFormat;
		this.uploadDir = uploadDir;
	}
	
	




	
	
	
	
	


}
