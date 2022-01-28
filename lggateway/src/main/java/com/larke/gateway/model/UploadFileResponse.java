package com.larke.gateway.model;

public class UploadFileResponse {

	private String name;
	private String fileDownloadUri;
	private String type;
	private long size;

	public UploadFileResponse(String name, String fileDownloadUri, String type, long size) {
		super();
		this.name = name;
		this.fileDownloadUri = fileDownloadUri;
		this.type = type;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
