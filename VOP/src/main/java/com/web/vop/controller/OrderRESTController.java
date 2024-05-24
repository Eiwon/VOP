package com.web.vop.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.OrderVO;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/order")
@Log4j
public class OrderRESTController {

	@Autowired
	OrderService orderService;
	
	// 주문 목록 요청
	//@GetMapping("/orderList")
	public ResponseEntity<List<OrderVO>> getOrderList(@AuthenticationPrincipal MemberDetails memberDetails){
		log.info("getOrderList()"); 
		String memberId = memberDetails.getUsername();
		log.info("memberId :" + memberId); 
		List<OrderVO> orderList = orderService.getOrderListByMemberId(memberId);
		log.info("orderlist : " + orderList);
		
		if(orderList != null && !orderList.isEmpty()) {
			return new ResponseEntity<List<OrderVO>>(orderList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<OrderVO>>(Collections.emptyList(), HttpStatus.OK);		
		}
		
	}//end getOrderList()
	
	
	
	
	
}
