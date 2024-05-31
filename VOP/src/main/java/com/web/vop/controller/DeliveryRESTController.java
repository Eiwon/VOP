package com.web.vop.controller;


import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
	public ResponseEntity<Integer> getDeliveryInfo(@PathVariable("paymentId") int paymentId) {
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
	public ResponseEntity<Integer> registerDelivery(@RequestBody DeliveryVO deliveryVO, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("����� ��� : " + deliveryVO);
		String memberId = memberDetails.getUsername();
		deliveryVO.setMemberId(memberId);
		int res = deliveryService.registerDelivery(deliveryVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerDelivery
	
	@PostMapping("/popupUpdate")
	@ResponseBody
	public ResponseEntity<Integer> updateDelivery(@RequestBody DeliveryVO deliveryVO, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("����� ���� ���� : " + deliveryVO);
		String memberId = memberDetails.getUsername();
		deliveryVO.setMemberId(memberId);
		int res = deliveryService.updateDelivery(deliveryVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateDelivery
	
	
	// �⺻ ������� �����Ǿ� �ִ��� Ȯ��
	@GetMapping("/checkDefaultAddress")
	public Map<String, Boolean> checkDefaultAddress(@RequestParam String memberId,
			@AuthenticationPrincipal MemberDetails memberDetails){
		
		log.info("checkDefaultAddress() - memberId : " + memberId);
		boolean hasDefault = deliveryService.hasDefaultAddress(memberId);
		log.info("�⺻ ����� ���� ? " + hasDefault);
		Map<String, Boolean> response = new HashMap<>();
		response.put("hasDefaultAddress", hasDefault);
		return response;
	}// end  checkDefaultAddress()
	
	
	// �ش��ϴ� memberid�� ������ �⺻����� ����� 0���� �ٲٱ� 
 	/*@PutMapping("/updateDefault")
	  public ResponseEntity<Integer> updateDefault(
			  @RequestBody String memberId) {
		 	log.info("updateDefault() -  �ش��ϴ� memberid�� ������ �⺻����� ����� 0���� �ٲٱ�");
        	log.info("memberId : " + memberId);
        	
        	int res = deliveryService.updateDefault(memberId);
        	log.info("�ش� ���̵��� �⺻ ����� ����� �����Ǿ����ϴ�.");
        
        	return new ResponseEntity<Integer>(res, HttpStatus.OK);        		  
 	}// end updateDefault()
	
        	
    // ����ϴ� deliveryId�� �⺻������� 1�� �ٲٱ�
 	@PutMapping("/updateNewDefault")
 	 public ResponseEntity<Integer> updateNewDefault(@RequestParam int deliveryId,
 			@AuthenticationPrincipal MemberDetails memberDetails){
 		log.info("updateNewDefault() - ����ϴ� deliveryId�� �⺻������� 1�� �ٲٱ�");
 		String memberId = memberDetails.getUsername(); 
    	log.info("memberId : " + memberId);
    	
 		int res = deliveryService.updateNewDefault(deliveryId, memberId);
 		log.info("�ش� ���̵��� �⺻ ����� ����� �����Ǿ����ϴ�.");
 		
 		return new ResponseEntity<Integer>(res, HttpStatus.OK);
 	}//end updateDefault()
 	*/
	
}//end DeliveryRESTController()
