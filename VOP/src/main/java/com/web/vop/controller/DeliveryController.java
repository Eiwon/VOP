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
	
	// 배송 예정일(OrderVO - expectDeliveryDate)을 조회한 결과를 배송조회 페이지에 전송
	// 예상배송일이 지났을 경우 ‘도착완료’ 업데이트
	@GetMapping("/delivery")
	public void deliveryGET(Model model) {
		System.out.println("delivery 페이지 이동");
		log.info("delivery 페이지 이동 요청");
		
	}
	
	
	
}
