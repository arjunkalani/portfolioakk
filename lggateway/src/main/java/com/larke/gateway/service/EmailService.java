package com.larke.gateway.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.context.annotation.ComponentScan;

import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
public interface EmailService {

	public void register(User user, String siteURL, String role);

	public boolean verify(String verificationCode);

	public void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException;

}
