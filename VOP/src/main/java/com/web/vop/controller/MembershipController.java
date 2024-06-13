package com.web.vop.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberDetails;
import com.web.vop.service.MembershipService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/membership")
@Controller
@Log4j
public class MembershipController {

	@Autowired
	MembershipService membershipService;
	
	@GetMapping("/register")
	public String registerGET(@AuthenticationPrincipal MemberDetails memberDetails){
		
		log.info("����� ��� ������ �̵�");
		
		String memberAuth = memberDetails.getAuth();
		log.info(memberAuth);
		
		if(memberAuth.equals("�����")) { // ���� ������ ������� ���
			log.info("success �������� �̵�");
			 return "membership/success"; // // ���� �������� ���𷺼�
		}else if(memberAuth.equals("�Ϲ�")){ // ���� ������ �Ϲ��� ���
			log.info("�Ϲ� �������� �̵�");
			return "membership/register"; // ��� �������� ���𷺼�
		}else {
			log.info("���� �Ұ�");
			return " ";
		}		
		
	}//end registerGET()
	
	
	@GetMapping("/success")
	public void successGET() {
		log.info("����� ��� ����");
	}
	
	
	
	
}
