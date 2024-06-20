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
		// �⺻�� : "�׼����� �źεǾ����ϴ�" alert ��� �� �ڷΰ��� �����Ű�� �������� �̵�
		
		log.info(request.getRequestURI()); // ���� �õ��� uri    ex) "/vop/seller/admin"
		String accessUri = request.getRequestURI();
		// ���⼭ uri�� ���� ó��?
		
		String[] splitUri = accessUri.split("[/?]"); // / �Ǵ� ? �������� ���ڿ� �ڸ���
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
