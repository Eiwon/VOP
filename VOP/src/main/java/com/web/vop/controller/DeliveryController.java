package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberVO;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/Delivery")
@Log4j
public class DeliveryController {
	
	@Autowired
	private MemberVO memberVO;
	
	
	// DB �� ����� ��� ������(OrderVO - expectDeliveryDate)�� ��ȸ
	// ���������� ������ ��� �������Ϸᡯ ������Ʈ
	@GetMapping("/delivery")
	public void deliveryGET() {
		System.out.println("delivery ������ �̵�");
		log.info("delivery ������ �̵� ��û");
	}
	
	
	
}
