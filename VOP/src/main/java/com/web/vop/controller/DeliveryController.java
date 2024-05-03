package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.service.MemberService;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/Delivery")
@Log4j
public class DeliveryController {
	
	@Autowired
	MemberService memberservice;
	
	@Autowired
	OrderService orderService;
	
	// ��� ������(OrderVO - expectDeliveryDate)�� ��ȸ�� ����� �����ȸ �������� ����
	// ���������� ������ ��� �������Ϸᡯ ������Ʈ
	@GetMapping("/delivery")
	public void deliveryGET(Model model) {
		System.out.println("delivery ������ �̵�");
		log.info("delivery ������ �̵� ��û");
		
	}
	
	
	
}
