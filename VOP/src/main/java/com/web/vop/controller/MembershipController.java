package com.web.vop.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.service.MembershipService;
import com.web.vop.util.PaymentAPIUtil;

import lombok.extern.log4j.Log4j;

@RequestMapping("/membership")
@Controller
@Log4j
public class MembershipController {

	@Autowired
	MembershipService membershipService;
	
	@Autowired
	private PaymentAPIUtil paymentAPIUtil;
	
	@GetMapping("/register")
	public String registerGET(@AuthenticationPrincipal MemberDetails memberDetails,
			Model model){
		log.info("����� ��� ������ �̵�");
		String memberId = memberDetails.getUsername();
		log.info(memberId);
		String memberAuth = memberDetails.getAuth();
		log.info(memberAuth);
	
		
		if(memberAuth.equals("�����")) { // ���� ������ ������� ���
			log.info("success �������� �̵�");
			 return "membership/success"; // // ���� �������� ���𷺼�
		}else if(memberAuth.equals("�Ϲ�")){ // ���� ������ �Ϲ��� ���
			log.info("�Ϲ� �������� �̵�");
			
			PaymentWrapper payment = membershipService.makeMembershipForm(memberId);
			log.info("paymentWrapper ��ü : " + payment);
			
			try { // �ڹٽ�ũ��Ʈ���� ���� ���� json ���� ���ڿ��� ��ȯ
				model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
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
