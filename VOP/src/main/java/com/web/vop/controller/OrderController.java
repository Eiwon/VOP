package com.web.vop.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/order")
@Controller
@Log4j
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	AWSS3Service awsS3Service;
	
	@GetMapping("/orderlist") 
	public void orderlistGET(Model model,@AuthenticationPrincipal MemberDetails memberDetails) { // 주문목록 페이지 불러오기
		log.info("orderlistGET()");
		
		List<OrderViewDTO> orderList = orderService.getOrderListByMemberId(memberDetails.getUsername());
	        
	    for(OrderViewDTO orderViewDTO : orderList) {
	        orderViewDTO.setImgUrl(
	        	awsS3Service.toImageUrl(orderViewDTO.getImgPath(), orderViewDTO.getImgChangeName())
	        );
	    }
	    log.info(orderList);
	    
	    Date now = new Date(); // 주문목록에서 현재날짜와 예상배송일을 비교하기 위해 생성
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 비교룰 위해 변환해줌
	    String formattedNow = dateFormat.format(now);
	    log.info("formattedNow : " + formattedNow); //현재날짜
	    
	    model.addAttribute("now", formattedNow);
	    
	    model.addAttribute("orderList", orderList);
	    
	}//end orderlistGET
	
	
	
	
	
}
