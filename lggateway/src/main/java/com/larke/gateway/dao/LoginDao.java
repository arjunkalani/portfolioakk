package com.larke.gateway.dao;

import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
public interface LoginDao {

	public User findUserInfo(String email);

	public List getUserRoles(long userid, long roleid);

}
