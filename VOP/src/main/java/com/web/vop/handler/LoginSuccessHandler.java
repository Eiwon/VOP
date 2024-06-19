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
		String uri = request.getContextPath(); // 메인페이지 경로 ("/vop")
		
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
		// 시큐리티로 인해 강제 이동시 돌아갈 URL
		String savedPage = (String) request.getSession().getAttribute("prevPage");
		// 직접 로그인 페이지 이동시 돌아갈 URL
		
		log.info("savedRequest : " + savedRequest + ", savedPage : " + savedPage);
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
