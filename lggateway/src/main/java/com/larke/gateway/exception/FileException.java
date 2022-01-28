package com.larke.gateway.exception;


	import org.springframework.context.annotation.ComponentScan;

	@ComponentScan(basePackages = "com.larke.gateway")
	public class FileException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public FileException(String message) {
			super(message);
		}

		public FileException(String message, Throwable cause) {
			super(message, cause);
		}
	}
