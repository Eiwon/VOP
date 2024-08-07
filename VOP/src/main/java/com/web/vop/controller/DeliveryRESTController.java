package com.web.vop.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.DeliveryListDTO;
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
	
	@Autowired
	DeliveryService deliveryService;
	
	// 배송지(delivery.jsp) 조회
	@GetMapping("getDeliveryList/{paymentId}")
	public ResponseEntity<List<DeliveryListDTO>> getDeliveryList(@PathVariable("paymentId") int paymentId){
		log.info("getDeliveryList()");
		log.info("송장번호(paymentId) : " + paymentId);
		List<DeliveryListDTO> deliveryList = deliveryService.getDeliveryList(paymentId);
		
		
		if(deliveryList == null || deliveryList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(deliveryList, HttpStatus.OK);
		}
	}//end getDeliveryList()
	
	
	
	// Delivery 정보를 JSON 형태로 반환한 API 엔드포인트
	@GetMapping("/getDeliveryInfo")
	public ResponseEntity<Integer> getDeliveryInfo(@PathVariable("paymentId") int paymentId) {
		log.info("getDeliveryInfo()");
		int result = orderService.getPaymentId(paymentId);
		log.info("paymentId : "  + result);
		return ResponseEntity.ok(result);
	}//end getDeliveryInfo()
	
	

	// 배송지 수정 페이지에서 deliveryId로 배송 조회
	@GetMapping("/restDeliveryUpdate")
    public ResponseEntity<DeliveryVO> getDeliveryInfoByDeliveryId(@RequestParam("deliveryId") int deliveryId,
    		@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        log.info("getDeliveryInfoByDeliveryId");
        
        // 인증된 사용자의 ID를 가져옵니다.
        String memberId = memberDetails.getUsername();
        log.info("memberId: " + memberId);
        
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId, memberId);
        log.info("delivery" + delivery);
        
        
        // 사용자가 선택한 배송지 선택 정보 View로 전달
        model.addAttribute("delivery", delivery);
        
        return new ResponseEntity<DeliveryVO>(delivery, HttpStatus.OK);
    }//end getDeliveryInfoByDeliveryId()
	
	
	// 배송지 수정 페이지에서 deliveryId로 삭제
	@DeleteMapping("delete/{deliveryId}")
	public ResponseEntity<Integer>deleteDelivery(@PathVariable int deliveryId){
		int res = deliveryService.deleteDelivery(deliveryId);
		log.info(res + "행 삭제");
		if(res == 1) {
			log.info("배송지 삭제 성공");
			return new ResponseEntity<>(res,HttpStatus.OK);
		}else {
			 log.error("배송지 삭제 실패");
		     return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
	}//end deleteDelivery()
	

	@GetMapping("/popupList")
	@ResponseBody
	public ResponseEntity<List<DeliveryVO>> getDeliveryList(@AuthenticationPrincipal MemberDetails memberDetails){
		log.info("배송지 목록 요청");
		
		String memberId = memberDetails.getUsername();
		List<DeliveryVO> deliveryList = deliveryService.getMemberId(memberId);
		
		return new ResponseEntity<List<DeliveryVO>>(deliveryList, HttpStatus.OK);
	} // end getDeliveryList
	
	@PostMapping("/popupRegister")
	@ResponseBody
	public ResponseEntity<Integer> registerDelivery(@RequestBody DeliveryVO deliveryVO, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("배송지 등록 : " + deliveryVO);
		String memberId = memberDetails.getUsername();
		deliveryVO.setMemberId(memberId);
		int res = deliveryService.registerDelivery(deliveryVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerDelivery
	
	@PostMapping("/popupUpdate")
	@ResponseBody
	public ResponseEntity<Integer> updateDelivery(@RequestBody DeliveryVO deliveryVO, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("배송지 정보 변경 : " + deliveryVO);
		String memberId = memberDetails.getUsername();
		deliveryVO.setMemberId(memberId);
		int res = deliveryService.updateDelivery(deliveryVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateDelivery
	
	
	// 기본 배송지가 설정되어 있는지 확인
	@GetMapping("/checkDefaultAddress")
	public Map<String, Boolean> checkDefaultAddress(@RequestParam String memberId,
			@AuthenticationPrincipal MemberDetails memberDetails){
		
		log.info("checkDefaultAddress() - memberId : " + memberId);
		boolean hasDefault = deliveryService.hasDefaultAddress(memberId);
		log.info("기본 배송지 존재 ? " + hasDefault);
		Map<String, Boolean> response = new HashMap<>();
		response.put("hasDefaultAddress", hasDefault);
		return response;
	}// end  checkDefaultAddress()
	
	
	// 배송지 목록에서 기본 배송지 목록을 체크 박스로 업데이트
 	@PutMapping("updateDefault/{memberId}/delivery/{deliveryId}")
	  public ResponseEntity<Integer> updateDefault(
			  @PathVariable("deliveryId") int deliveryId,
			  @PathVariable("memberId") String memberId) {
		 	log.info("updateDefault()");
        	log.info("memberId : " + memberId);
        	log.info("deliveryId : " + deliveryId);
        	
        	// 배송지의 기본배송지를 1로 수정
        	int res = deliveryService.updateNewDefault(deliveryId, memberId);
        	log.info("res : " + res);
       
        	
        	return new ResponseEntity<Integer>(res, HttpStatus.OK);        		  
 	}// end updateDefault()
	
   
	
}//end DeliveryRESTController()
