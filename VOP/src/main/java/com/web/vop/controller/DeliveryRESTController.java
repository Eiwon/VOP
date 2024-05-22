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
	

	// 배송지 수정 페이지에서 deliveryId로 배송 조회

	@Autowired
	DeliveryService deliveryService;
	
	// Delivery 정보를 JSON 형태로 반환한 API 엔드포인트
	@GetMapping("/getDeliveryInfo")
	public ResponseEntity<Integer> getDeliveryInfo(@RequestParam("paymentId") int paymentId) {
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
	@PostMapping("/delete")
	public ResponseEntity<Integer>deleteDelivery(@Param("deliveryId")int deliveryId){
		int res = deliveryService.deleteDelivery(deliveryId);
		log.info(res + "행 삭제");
		return new ResponseEntity<Integer>(res,HttpStatus.OK);
	}
	

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
	public ResponseEntity<Integer> registerDelivery(@RequestBody DeliveryVO deliveryVO, @AuthenticationPrincipal MemberDetails memberDetails){
		log.info("배송지 등록 : " + deliveryVO);
		String memberId = memberDetails.getUsername();
		deliveryVO.setMemberId(memberId);
		int res = deliveryService.registerDelivery(deliveryVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerDelivery
	
	
	// 기본 배송지가 설정되어 있는지 확인
	@GetMapping("/checkDefaultAddress")
	public Map<String, Boolean> checkDefaultAddress(@RequestParam String memberId){
		log.info("checkDefaultAddress() - memberId : " + memberId);
		boolean hasDefault = deliveryService.hasDefaultAddress(memberId);
		log.info("기본 배송지 존재 ? " + hasDefault);
		Map<String, Boolean> response = new HashMap<>();
		response.put("hasDefaultAddress", hasDefault);
		return response;
	}// end  checkDefaultAddress()
	
	
	// 기존 배송지의 기본 배송지 = 0 으로 업데이트
 	@PutMapping("/delivery/updateDefault")
	  public ResponseEntity<String> updateDefault(@RequestBody Map<String, Integer> requestMap,
			  @AuthenticationPrincipal MemberDetails memberDetails) {
		 		log.info("updateDefault()");
        		int newDefaultDeliveryId = requestMap.get("deliveryId");
        		String memberId = memberDetails.getUsername(); // 실제로는 세션 등에서 가져오는 코드 필요
        		log.info("memberId : " + memberId);
        		
	        try {
            	// 기본 배송지를 업데이트하는 서비스 메서드 호출
            	deliveryService.setDefaultDelivery(newDefaultDeliveryId, memberId);
            	return ResponseEntity.ok("기본 배송지가 성공적으로 업데이트되었습니다.");
        		} catch (Exception e) {
            	log.error("기본 배송지 업데이트 중 오류 발생: " + e.getMessage());
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("기본 배송지 업데이트 중 오류가 발생했습니다.");
        	}
    }// end updateDefault()
	

	
	
	
}//end DeliveryRESTController()
