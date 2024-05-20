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
    public ResponseEntity<DeliveryVO> getDeliveryInfoByDeliveryId(@RequestParam("deliveryId") int deliveryId,@RequestParam("memberId") String memberId, Model model) {
        log.info("getDeliveryInfoByDeliveryId");
        
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId, memberId);
        log.info("delivery" + delivery);
        
        
        // 사용자가 선택한 배송지 선택 정보 View로 전달
        model.addAttribute("delivery", delivery);
        
        // 사용자가 선택한 배송지의 deliveryId View로 전달
        model.addAttribute("deliveryId", deliveryId);
        
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
	public ResponseEntity<List<DeliveryVO>> getDeliveryList(HttpServletRequest request){
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("배송지 목록 요청");

		
		List<DeliveryVO> deliveryList = new ArrayList<>();
		deliveryList.add(new DeliveryVO(1, "test1234", "test", "test", "01012341234", "문앞", "상세", 1));
		
		return new ResponseEntity<List<DeliveryVO>>(deliveryList, HttpStatus.OK);
	} // end getDeliveryList
	
	@PostMapping("/popupRegister")
	@ResponseBody
	public ResponseEntity<Integer> registerDelivery(@RequestBody DeliveryVO deliveryVO, HttpServletRequest request){
		log.info("배송지 등록");
		String memberId = (String)request.getSession().getAttribute("memberId");
		deliveryVO.setMemberId(memberId);
		
		// 등록
		int res = 1;
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerDelivery
	
}//end DeliveryRESTController()
