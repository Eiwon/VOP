
package com.web.vop.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenAuthenticationService {

	String getUsernameFromToken(String token);
	
	String createToken(UserDetails memberDetails);
	
	String createAccessToken(UserDetails memberDetails);
	
	String createRefreshToken(UserDetails memberDetails);
	
	UserDetails getUserFromToken(String token);
	
	boolean isValidToken(String token, HttpServletResponse response);
	
	String extractAccessToken(HttpServletRequest request);
	
	String extractRefreshToken(HttpServletRequest request);
}
