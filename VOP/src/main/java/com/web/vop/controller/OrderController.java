package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.OrderVO;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/order")
@Controller
@Log4j
public class OrderController {

	@Autowired
	OrderService orderService;
	
	//@GetMapping("/orderlist") 
	public void orderlistGET() { // 주문목록 페이지 불러오기
		System.out.println("orderlist.jsp 이동");
		log.info("orderlistGET()");
	
	}//end orderlistGET()
	
	
	
	
	
}
