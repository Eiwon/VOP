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
import com.web.vop.util.FileUploadUtil;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/order")
@Log4j
public class OrderRESTController {

	@Autowired
	OrderService orderService;
	
	@Autowired
    AWSS3Service awsS3Service;
	
	// 주문 목록 요청
	@GetMapping("/myOrder")
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
	
	
	
	// 주문목록 이미지 파일
	@GetMapping("/showImg/{imgId}")
	@ResponseBody
	public ResponseEntity<String> showOrderImg(@PathVariable("imgId") int imgId) {
		 log.info("showOrderImg() : " + imgId);
		 String imgUrl = awsS3Service.getImageUrl(imgId);

	return new ResponseEntity<String>(imgUrl, HttpStatus.OK);
		}
		
	
	
	
}
