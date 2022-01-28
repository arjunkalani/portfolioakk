package com.larke.gateway.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;
import com.larke.gateway.repository.RoleRepository;
import com.larke.gateway.repository.UserRepository;
import net.bytebuddy.utility.RandomString;

@Service("emailService")
@ComponentScan(basePackages = "com.larke.gateway")
public class EmailServiceImpl implements EmailService {

	static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	private static final String DATETIME = "yyyy-MM-dd HH:mm:ss";

	public void register(User user, String siteURL, String roles) {
		user.setId(user.getId());
		user.setEmail(user.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setFirstname(user.getFirstname());
		user.setLastname(user.getLastname());
		user.setActive(0);
		Role roleUser = roleRepository.findByRole("User");
		user.addRole(roleUser);
		user.setEnabled(false);
		user.setAccountlocked(false);
		user.setAccountexpired(false);
		user.setCredentialsexpired(false);
		user.setCreatedtime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME)));
		user.setUpdatedtime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME)));
		String randomCode = RandomString.make(64);
		user.setConfirmationtoken(randomCode);
		user.setVerificationCode(randomCode);
		try {
			sendVerificationEmail(user, siteURL);
			LOGGER.info("REPORTING FROM register user id step 1: ", user.getId());
			LOGGER.info("REPORTING FROM register email step 2: ", user.getEmail());
			LOGGER.info("REPORTING FROM register First Name step 3: ", user.getFirstname());
			LOGGER.info("REPORTING FROM register Last Name step 4: ", user.getLastname());
			LOGGER.info("REPORTING FROM register Full Name step 5: ", user.getFullName());
			LOGGER.info("REPORTING FROM register Active step 6: ", user.getActive());
			LOGGER.info("REPORTING FROM register Enabled step 7: ", user.isEnabled());
			LOGGER.info("REPORTING FROM register Account Locked step 8: ", user.isAccountlocked());
			LOGGER.info("REPORTING FROM register Account Expired step 9: ", user.isAccountexpired());
			LOGGER.info("REPORTING FROM register Credentials Expired step 10: ", user.isCredentialsexpired());
			LOGGER.info("REPORTING FROM register Created Time step 11: ", user.getCreatedtime());
			LOGGER.info("REPORTING FROM register Updated Time step 12: ", user.getUpdatedtime());
			LOGGER.info("REPORTING FROM register Conformation Token step 13: ", user.getConfirmationtoken());
			LOGGER.info("REPORTING FROM register Verification Token step 14: ", user.getVerificationCode());
			LOGGER.info("REPORTING FROM register siteURL step 15: ", siteURL);
			LOGGER.info("REPORTING FROM register Roles step 16: ", roles);
			LOGGER.info("REPORTING FROM register Roles step 17: ");
			LOGGER.info("REPORTING FROM register Roles step 18: ", user.getRoles());
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		userRepository.save(user);
	}

	public void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "arjunkalani@gmail.com";
		String senderName = "Larke Group, LLC";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "user id: " + user.getId() + "<br>"
				+ "user email: " + user.getEmail() + "<br>" + "user roles: " + user.getRoles() + "<br>"
				+ "---------------------" + "Thank you,<br>" + "Larke Group, LLC.";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		content = content.replace("[[name]]", user.getFullName());
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content, true);
		LOGGER.info("REPORTING FROM sendVerificationEmail before sending: ");
		mailSender.send(message);
		LOGGER.info("REPORTING FROM sendVerificationEmail after USER Verify Code: ", user.getVerificationCode());
		LOGGER.info(siteURL, "/verify?code=", user.getVerificationCode());
		LOGGER.info("Email has been sent");
	}

	public boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);
		if (user == null || user.isEnabled()) {
			return false;
		} else {
			user.setActive(1);
			user.setEnabled(true);
			user.setVerificationCode(null);
			user.setUpdatedtime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME)));
			userRepository.save(user);
			LOGGER.info("REPORTING FROM register Active step 6: ", user.getActive());
			LOGGER.info("REPORTING FROM register Enabled step 7: ", user.isEnabled());
			LOGGER.info("REPORTING FROM register Verification Token step 14: ", user.getVerificationCode());
			LOGGER.info("REPORTING FROM register Updated Time step 12: ", user.getUpdatedtime());
			return true;
		}
	}

}