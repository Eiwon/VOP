package com.web.vop.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j;

@Log4j
public class SecurityAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String redirectUri = request.getContextPath() + "/access/denied";
		// 기본값 : "액세스가 거부되었습니다" alert 띄운 후 뒤로가기 실행시키는 페이지로 이동
		
		log.info(request.getRequestURI()); // 접근 시도한 uri    ex) "/vop/seller/admin"
		String accessUri = request.getRequestURI();
		// 여기서 uri에 따라 처리?
		
		String[] splitUri = accessUri.split("[/?]"); // / 또는 ? 기준으로 문자열 자르기
		String accessController = splitUri[2]; // ex) seller 
		String accessMethod = splitUri[3]; // ex) admin
		
		if(accessController.equals("seller")) {
			if(accessMethod.equals("main")) {
				redirectUri = "sellerRequest";
			}
		}
		
		response.sendRedirect(redirectUri);
	}

}
