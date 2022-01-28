package com.larke.gateway.exception;


	import org.springframework.context.annotation.ComponentScan;

	@ComponentScan(basePackages = "com.larke.gateway")
	public class FileNotFoundException extends FileException {

		private static final long serialVersionUID = 1L;

		public FileNotFoundException(String message) {
			super(message);
		}

		public FileNotFoundException(String message, Throwable cause) {
			super(message, cause);
		}
	}
