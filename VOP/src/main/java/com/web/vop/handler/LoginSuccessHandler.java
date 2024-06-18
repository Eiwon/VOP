package com.web.vop.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import lombok.extern.log4j.Log4j;

@Log4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String uri = request.getContextPath();
		
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
		String savedPage = (String) request.getSession().getAttribute("prevPage");
		
		if(savedRequest != null) {
			uri = savedRequest.getRedirectUrl();
		}else if (savedPage != null){
			uri = savedPage;
			request.getSession().removeAttribute("prevPage");
		}
		log.info("로그인 성공");
		response.sendRedirect(uri);
	}
	
}
