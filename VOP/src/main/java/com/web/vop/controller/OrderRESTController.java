package com.web.vop.controller;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.OrderVO;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.ImageService;
import com.web.vop.service.OrderService;
import com.web.vop.socket.AlarmHandler;
import com.web.vop.util.FileAnalyzerUtil;

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
