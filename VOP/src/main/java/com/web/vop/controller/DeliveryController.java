package com.web.vop.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.service.DeliveryService;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;


@Controller
@RequestMapping("/delivery")
@Log4j
public class DeliveryController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	private DeliveryService deliveryService;
	
	// �������������� �ֹ���� > �����ȸ �������� �̵�
	@GetMapping("/delivery")
	public void deliveryGET() {
		log.info("delivery ������ �̵� ��û");
	}//end deliveryGET()
	
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("��� ���� ��� ������ �̵�");
	} // end registerGET
	
	@PostMapping("/register")
	public String registerPOST(DeliveryVO deliveryVO, HttpServletRequest request) {
		log.info("��� ���� ���");
		log.info("��� ���� : " + deliveryVO);
		
		deliveryVO.setMemberId((String)request.getSession().getAttribute("memberId"));
		
		int res = deliveryService.registerDelivery(deliveryVO);
		log.info(res + "�� ��� ����");
		
		return "redirect:delivery";
	} // end registerPOST
	
}
