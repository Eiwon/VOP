package com.web.vop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/order")
@Log4j
public class OrderRESTController {

	@Autowired
	OrderService orderService;

	// 주문 목록 삭제
	@DeleteMapping("deleteOrder/{orderId}")
	public ResponseEntity<Integer> deleteOrderListByOrderId(@PathVariable int orderId){
		log.info("OrderListDELETE() - orderId : " + orderId);
		int res = orderService.deleteOrderListByOrderId(orderId);
			
		if(res == 1) {
			return new ResponseEntity<>(res, HttpStatus.OK);
		}else {
			return ResponseEntity.status(500).body(res);
		}
	}//end membershipDELETE()
	
}//end OrderRESTController()
