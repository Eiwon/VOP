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
	
	@GetMapping("/orderlist") 
	public String orderlistGET(Model model,@AuthenticationPrincipal MemberDetails memberDetails) { // 주문목록 페이지 불러오기
		log.info("orderlistGET()");
		
		// 사용자가 로그인한 경우에만 주문 목록을 표시하도록 합니다.
	    if (memberDetails != null && memberDetails.getUsername() != null) {
	        List<OrderVO> orderList = orderService.getOrderListByMemberId(memberDetails.getUsername());
	        log.info(orderList);
	        model.addAttribute("orderList", orderList);
	        model.addAttribute("memberDetails", memberDetails);
	        return "order/orderlist"; // 주문 목록 페이지로 이동
	    } else {
	        // 로그인하지 않은 경우에는 로그인 페이지로 리다이렉트
	        return "redirect:/member/login";
	    }

	  
	}//end orderlistGET
	
	
	
	
	
}
