package com.web.vop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.service.DeliveryService;
import com.web.vop.service.OrderService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/Delivery")
@Log4j
public class DeliveryRESTController {

	@Autowired
	OrderService orderService;
	
	// Delivery 정보를 JSON 형태로 반환한 API 엔드포인트
	@GetMapping("/getDeliveryInfo")
	public ResponseEntity<Integer> getDeliveryInfo(@RequestParam("paymentId") int paymentId) {
		log.info("getDeliveryInfo()");
		int result = orderService.getPaymentId(paymentId);
		log.info("paymentId : "  + result);
		return ResponseEntity.ok(result);
	}//end getDeliveryInfo()
	
	
	// 배송지 수정 페이지에서 deliveryId로 배송 조회
	@Autowired
	DeliveryService deliveryService;
	@GetMapping("/restDeliveryUpdate")
    public ResponseEntity<DeliveryVO> getDeliveryInfoByDeliveryId(@RequestParam("deliveryId") int deliveryId, Model model) {
        log.info("getDeliveryInfoByDeliveryId 요청, deliveryId: " + deliveryId);
        
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId);
        log.info("delivery" + delivery);
        
        
     // 사용자가 선택한 배송지 선택 정보 View로 전달
        model.addAttribute("delivery", delivery);
        
        // 사용자가 선택한 배송지의 deliveryId View로 전달
        model.addAttribute("deliveryId", deliveryId);
        
        return new ResponseEntity<DeliveryVO>(delivery, HttpStatus.OK);
    }//end getDeliveryInfoByDeliveryId()
	
	
		
}//end DeliveryRESTController()
