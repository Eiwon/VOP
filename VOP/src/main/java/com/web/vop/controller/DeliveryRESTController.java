package com.web.vop.controller;


import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberDetails;
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
    public ResponseEntity<DeliveryVO> getDeliveryInfoByDeliveryId(@RequestParam("deliveryId") int deliveryId,
    		@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        log.info("getDeliveryInfoByDeliveryId");
        
        // ������ ������� ID�� �����ɴϴ�.
        String memberId = memberDetails.getUsername();
        log.info("memberId: " + memberId);
        
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId, memberId);
        log.info("delivery" + delivery);
        
        
        // ����ڰ� ������ ����� ���� ���� View�� ����
        model.addAttribute("delivery", delivery);
        
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
	public ResponseEntity<List<DeliveryVO>> getDeliveryList(@AuthenticationPrincipal MemberDetails memberDetails){
		log.info("����� ��� ��û");
		
		String memberId = memberDetails.getUsername();
		List<DeliveryVO> deliveryList = deliveryService.getMemberId(memberId);
		
		return new ResponseEntity<List<DeliveryVO>>(deliveryList, HttpStatus.OK);
	} // end getDeliveryList
	
	@PostMapping("/popupRegister")
	@ResponseBody
	public ResponseEntity<Integer> registerDelivery(@RequestBody DeliveryVO deliveryVO, @AuthenticationPrincipal MemberDetails memberDetails){
		log.info("����� ��� : " + deliveryVO);
		String memberId = memberDetails.getUsername();
		deliveryVO.setMemberId(memberId);
		int res = deliveryService.registerDelivery(deliveryVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerDelivery
	
	
	// �⺻ ������� �����Ǿ� �ִ��� Ȯ��
	@GetMapping("/checkDefaultAddress")
	public Map<String, Boolean> checkDefaultAddress(@RequestParam String memberId){
		log.info("checkDefaultAddress() - memberId : " + memberId);
		boolean hasDefault = deliveryService.hasDefaultAddress(memberId);
		log.info("�⺻ ����� ���� ? " + hasDefault);
		Map<String, Boolean> response = new HashMap<>();
		response.put("hasDefaultAddress", hasDefault);
		return response;
	}// end  checkDefaultAddress()
	
	
	// ���� ������� �⺻ ����� = 0 ���� ������Ʈ
 	@PutMapping("/delivery/updateDefault")
	  public ResponseEntity<String> updateDefault(@RequestBody Map<String, Integer> requestMap,
			  @AuthenticationPrincipal MemberDetails memberDetails) {
		 		log.info("updateDefault()");
        		int newDefaultDeliveryId = requestMap.get("deliveryId");
        		String memberId = memberDetails.getUsername(); // �����δ� ���� ��� �������� �ڵ� �ʿ�
        		log.info("memberId : " + memberId);
        		
	        try {
            	// �⺻ ������� ������Ʈ�ϴ� ���� �޼��� ȣ��
            	deliveryService.setDefaultDelivery(newDefaultDeliveryId, memberId);
            	return ResponseEntity.ok("�⺻ ������� ���������� ������Ʈ�Ǿ����ϴ�.");
        		} catch (Exception e) {
            	log.error("�⺻ ����� ������Ʈ �� ���� �߻�: " + e.getMessage());
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("�⺻ ����� ������Ʈ �� ������ �߻��߽��ϴ�.");
        	}
    }// end updateDefault()
	

	
	
	
}//end DeliveryRESTController()
