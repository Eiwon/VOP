package com.web.vop.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			UserDetails memberDetails = (UserDetails) authentication.getPrincipal();
			String memberId = memberDetails.getUsername();
			log.info("로그아웃 성공 : " + memberId);
			super.onLogoutSuccess(request, response, authentication);
		} // end onLogoutSuccess
	
	
}
