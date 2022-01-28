package com.larke.gateway.configuration;

import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import com.larke.gateway.service.CustomUserDetailsService;
import com.larke.gateway.validator.RoleValidator;
import lombok.var;

@ComponentScan(basePackages = "com.larke.gateway")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CustomLogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private CustomLoginSuccessHandler loginSuccessHandler;

	public CustomLogoutSuccessHandler getLogoutSuccessHandler() {
		return logoutSuccessHandler;
	}

	public void setLogoutSuccessHandler(CustomLogoutSuccessHandler logoutSuccessHandler) {
		this.logoutSuccessHandler = logoutSuccessHandler;
	}

	public CustomLoginSuccessHandler getLoginSuccessHandler() {
		return loginSuccessHandler;
	}

	public void setLoginSuccessHandler(CustomLoginSuccessHandler loginSuccessHandler) {
		this.loginSuccessHandler = loginSuccessHandler;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	private final String USERS_QUERY = "select email, password, enabled from user where email=? ";
	private final String ROLES_QUERY = "select u.email, r.role from user u inner join user_role ur on (u.id = ur.user_id) inner join role r on (ur.role_id=r.role_id) where u.email=?";

	protected void configure(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery(USERS_QUERY).authoritiesByUsernameQuery(ROLES_QUERY)
				.dataSource(dataSource).passwordEncoder(bCryptpasswordEncoder()).and()
				.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(bCryptpasswordEncoder());
		return authProvider;
	}

	protected void configure(HttpSecurity http, DataSource dataSource) throws Exception {
		LOGGER.info("REPORTING FROM /config http: ");
		http.csrf().disable();
		http

				.authorizeRequests()
						.antMatchers("/").hasAnyAuthority("USER", "DBA", "EDITOR", "ADMIN")
						.antMatchers("/dashboard/admin/**", "/adminDashboard", "dashboard/admin/adminDashboard").hasRole("ADMIN")
						.antMatchers("/delete/{id}", "/userManagement", "/documentManagement").hasAuthority("ADMIN")
						.antMatchers("/dashboard/user/**", "/userDashboard").hasAnyAuthority("USER", "DBA", "EDITOR")
						.antMatchers("/view/{id}", "/userListRole", "/userList").hasAnyAuthority("USER", "DBA", "EDITOR")
						.antMatchers("/edit/{id}").hasAnyAuthority("DBA", "EDITOR")
						.antMatchers("/dashboard/**", "/dashboard/secureWelcome", "/secureWelcome").hasAnyAuthority("USER", "DBA", "EDITOR", "ADMIN")
						.antMatchers("/index", "/greeting", "/esignup").permitAll()
						.antMatchers("/login", "/loginPage", "/login_in_process").permitAll()
						.antMatchers("/process_register", "/verify").permitAll()
						.antMatchers("/static/**", "/static/loginPage", "/static/esignup", "/static/index","/static/registerSuccess").permitAll()
						.antMatchers("/logout").permitAll()
						.antMatchers("/open/**", "/js/**", "/css/**", "/img/**", "/fragments/**", "/webjars/**").permitAll()
						.antMatchers("/uploads/**").permitAll()
				.and()
					.formLogin()
						.loginPage("/static/loginPage")
						.successHandler(loginSuccessHandler).permitAll()
				.and()
					.logout()
						.logoutRequestMatcher(new AntPathRequestMatcher("/static/loginPage?logout"))
						.logoutSuccessHandler(logoutSuccessHandler)
						.logoutSuccessUrl("/static/loginPage")
						.clearAuthentication(true).permitAll()
				.and()
					.rememberMe()
						.tokenValiditySeconds(7 * 24 * 60 * 60) // expiration time: 7 days
						.key("AbcdefghiJklmNoPqRstUvXyz")
						.tokenRepository(persistentTokenRepository(dataSource))
				.and()
					.exceptionHandling()
						.accessDeniedPage("/error/access_denied")
				.and()
					.httpBasic();
				super.configure(http);
	}

	public SpringResourceTemplateResolver templateResolver() {
		var templateResolver = (var) new SpringResourceTemplateResolver();
		((WebSecurityConfigurerAdapter) templateResolver).setApplicationContext(applicationContext);
		((AbstractConfigurableTemplateResolver) templateResolver).setPrefix("/templates/");
		((AbstractConfigurableTemplateResolver) templateResolver).setSuffix(".html");
		return (SpringResourceTemplateResolver) templateResolver;
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

	@Bean
	public RoleValidator roleValidator() {
		return new RoleValidator();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		String host = "smtp.mailtrap.io";
		String from = "arjunkalani@gmail.com";
		final String username = "ce2349a8cdb8e1";
		final String password = "7a77753f846ee6";
		mailSender.setHost("smtp.mailtrap.io");
		mailSender.setPort(587);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		return mailSender;
	}

	@Bean
	public BCryptPasswordEncoder bCryptpasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public CustomLogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	public CustomLoginSuccessHandler loginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

}
