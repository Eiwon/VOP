package com.web.vop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.domain.ProductVO;
import com.web.vop.service.BasketService;
import com.web.vop.service.CouponService;
import com.web.vop.service.DeliveryService;
import com.web.vop.service.MemberService;
import com.web.vop.service.OrderService;
import com.web.vop.service.PaymentService;
import com.web.vop.service.ProductService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/payment")
@Log4j
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/checkout")
	public void makePayment(Model model, int[] productIds, int[] productNums, String memberId) {
		log.info("makePayment() - memberId : " + memberId);
		log.info("결제할 상품 갯수 : " + productIds.length);
		
		List<OrderVO> orderList = new ArrayList<>();
		
		PaymentWrapper payment = paymentService.makePaymentForm(productIds, productNums, memberId);
		
		try { // 자바스크립트에서 쓰기 위해 json 형식 문자열로 변환
			model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} // end makePayment
	
	
	@GetMapping("/paymentResult")
	public void paymentResultGET() {
		log.info("paymentResult.jsp 이동 요청");
	} // end paymentResultGET
	
	
	@GetMapping("/getId")
	@ResponseBody
	public ResponseEntity<Integer> getNewPaymentId(){
		log.info("새로운 paymentId 발급 요청");
		int paymentId = paymentService.getNewPaymentId();
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	} // end getNewPaymentId
	
	
	@PostMapping("/apply")
	@ResponseBody
	public ResponseEntity<Integer> savePaymentResult(@RequestBody PaymentWrapper paymentResult){
		log.info("---------결제 결과 저장--------");
		log.info("결제 내역 : " + paymentResult.getPaymentVO());
		log.info("주문 목록 : " + paymentResult.getOrderList());
		log.info("쿠폰 사용 내역 : " + paymentResult.getCouponVO());
		
		int res = paymentService.registerPayment(paymentResult); // 결제 결과 등록
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end savePaymentResult
	
	@GetMapping("/payment")
	@ResponseBody
	public ResponseEntity<PaymentWrapper> getPaymentResult(HttpServletRequest request){
		log.info("결제 결과 조회 요청");
		String memberId = (String) request.getSession().getAttribute("memberId");// 각 주문정보와 전체 결제 내역을 한번에 보내기 위해 포장
		PaymentWrapper payment = paymentService.getRecentPayment(memberId);
		
		return new ResponseEntity<PaymentWrapper>(payment, HttpStatus.OK);
	} // end sendPaymentResult
	
	
	@GetMapping("/popupDeliverySelect")
	public String popupDeliverySelect() {
		log.info("배송지 선택 팝업 호출");
		return "redirect:../Delivery/popupSelect";
	} // end popupDeliverySelect
	
	
}
