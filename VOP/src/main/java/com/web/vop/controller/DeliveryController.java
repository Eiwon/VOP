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
	
	
	// DB 에 저장된 배송 예정일(OrderVO - expectDeliveryDate)을 조회
	// 예상배송일이 지났을 경우 ‘도착완료’ 업데이트
	@GetMapping("/delivery")
	public void deliveryGET() {
		System.out.println("delivery 페이지 이동");
		log.info("delivery 페이지 이동 요청");
	}
	
	
	
}
