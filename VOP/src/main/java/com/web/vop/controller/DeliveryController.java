package com.web.vop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/Delivery")
@Log4j
public class DeliveryController {
	
	@Autowired
	OrderService orderService;
	
	// �������������� �ֹ���� > �����ȸ �������� �̵�
	@GetMapping("/delivery")
	public void deliveryGET() {
		log.info("delivery ������ �̵� ��û");
	}//end deliveryGET()
	
	
	
	
	
	
}
