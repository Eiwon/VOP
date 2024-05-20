package com.web.vop.controller;


import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	

	// ����� ���� ���������� deliveryId�� ��� ��ȸ

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
	

	@GetMapping("/popupList")
	@ResponseBody
	public ResponseEntity<List<DeliveryVO>> getDeliveryList(HttpServletRequest request){
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("����� ��� ��û");

		
		List<DeliveryVO> deliveryList = new ArrayList<>();
		deliveryList.add(new DeliveryVO(1, "test1234", "test", "test", "01012341234", "����", "��", 1));
		
		return new ResponseEntity<List<DeliveryVO>>(deliveryList, HttpStatus.OK);
	} // end getDeliveryList
	
	@PostMapping("/popupRegister")
	@ResponseBody
	public ResponseEntity<Integer> registerDelivery(@RequestBody DeliveryVO deliveryVO, HttpServletRequest request){
		log.info("����� ���");
		String memberId = (String)request.getSession().getAttribute("memberId");
		deliveryVO.setMemberId(memberId);
		
		// ���
		int res = 1;
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerDelivery
	
}//end DeliveryRESTController()
