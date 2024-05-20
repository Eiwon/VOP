package com.web.vop.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@Autowired
	DeliveryService deliveryService;
	
	// Delivery ������ JSON ���·� ��ȯ�� API ��������Ʈ
	@GetMapping("/getDeliveryInfo")
	public ResponseEntity<Integer> getDeliveryInfo(@RequestParam("paymentId") int paymentId) {
		log.info("getDeliveryInfo()");
		int result = orderService.getPaymentId(paymentId);
		log.info("paymentId : "  + result);
		return ResponseEntity.ok(result);
	}//end getDeliveryInfo()
	
	
	// ����� ���� ���������� deliveryId�� ��� ��ȸ
	
	@GetMapping("/restDeliveryUpdate")
    public ResponseEntity<DeliveryVO> getDeliveryInfoByDeliveryId(@RequestParam("deliveryId") int deliveryId,@RequestParam("memberId") String memberId, Model model) {
        log.info("getDeliveryInfoByDeliveryId");
        
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId, memberId);
        log.info("delivery" + delivery);
        
        
        // ����ڰ� ������ ����� ���� ���� View�� ����
        model.addAttribute("delivery", delivery);
        
        // ����ڰ� ������ ������� deliveryId View�� ����
        model.addAttribute("deliveryId", deliveryId);
        
        return new ResponseEntity<DeliveryVO>(delivery, HttpStatus.OK);
    }//end getDeliveryInfoByDeliveryId()
	
	
	// ����� ���� ���������� deliveryId�� ����
	@PostMapping("/delete")
	public ResponseEntity<Integer>deleteDelivery(@Param("deliveryId")int deliveryId){
		int res = deliveryService.deleteDelivery(deliveryId);
		log.info(res + "�� ����");
		return new ResponseEntity<Integer>(res,HttpStatus.OK);
	}
	
		
}//end DeliveryRESTController()
