package com.web.vop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;
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
import com.web.vop.util.PaymentAPIUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/payment")
@Log4j
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private PaymentAPIUtil paymentAPIUtil;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/checkout")
	public void makePayment(
			Model model, int[] productIds, int[] productNums, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("makePayment()");
		log.info("결제할 상품 갯수 : " + productIds.length);
		
		PaymentWrapper payment = paymentService.makePaymentForm(productIds, productNums, memberDetails.getUsername());
		
		try { // 자바스크립트에서 쓰기 위해 json 형식 문자열로 변환
			model.addAttribute("paymentWrapper", new ObjectMapper().writeValueAsString(payment));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} // end makePayment
	
	
	@GetMapping("/paymentResult")
	public void paymentResultGET(Model model, int paymentId , @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("paymentResult.jsp 이동 요청");
		String memberId = memberDetails.getUsername();
		// 각 주문정보와 전체 결제 내역을 한번에 보내기 위해 포장
		PaymentWrapper paymentWrapper = paymentService.getPayment(memberId, paymentId);
		
		model.addAttribute("paymentWrapper", paymentWrapper);
		
	} // end paymentResultGET
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getId")
	@ResponseBody
	public ResponseEntity<Integer> getNewPaymentId(){
		log.info("새로운 paymentId 발급 요청");
		int paymentId = paymentService.getNewPaymentId();
		return new ResponseEntity<Integer>(paymentId, HttpStatus.OK);
	} // end getNewPaymentId
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/apply")
	@ResponseBody
	public ResponseEntity<Integer> savePaymentResult(@RequestBody PaymentWrapper paymentResult){
		log.info("---------결제 결과 저장--------");
		log.info("결제 내역 : " + paymentResult.getPaymentVO());
		log.info("주문 목록 : " + paymentResult.getOrderList());
		log.info("쿠폰 사용 내역 : " + paymentResult.getMyCouponVO());
		
		PaymentVO paymentVO = paymentResult.getPaymentVO();
		String impUid = paymentVO.getChargeId();
		int chargePrice = paymentVO.getChargePrice();
		int res = 0;
		// 결제 정보가 유효한지 검사
		// 주문 목록 총액이 결제금액과 일치하는지 확인
		int total = 0;
		for(OrderViewDTO orderDTO : paymentResult.getOrderList()) {
			total += orderDTO.getOrderVO().getProductPrice() * orderDTO.getOrderVO().getPurchaseNum();
		}
		total = (total + paymentVO.getDeliveryPrice()) * 
				(100 - paymentVO.getMembershipDiscount() - paymentVO.getCouponDiscount()) / 100;
				
		if(total != chargePrice) {
			// 결제 에러
			log.error("구매물품 총액 : 결제액 불일치");
			paymentAPIUtil.cancelPayment(impUid); // 결제 취소
			return new ResponseEntity<Integer>(res, HttpStatus.OK);
		}
		
		// 서버로 전달된 금액이 실제 결제 금액과 같은지 확인
		int realChargePrice = paymentAPIUtil.getPaymentAmount(impUid);
		if(realChargePrice != chargePrice) {
			// 결제 에러
			log.error("실결제액 : 서버 전송 결제액 불일치");
			paymentAPIUtil.cancelPayment(impUid); // 결제 취소
			return new ResponseEntity<Integer>(res, HttpStatus.OK);
		}
		
		try {
			res = paymentService.registerPayment(paymentResult); // 결제 결과 등록
		}catch(DataIntegrityViolationException e) {
			log.error("DB 저장 실패 : 재고 부족");
			paymentAPIUtil.cancelPayment(impUid); // 결제 취소
			res = -1;
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end savePaymentResult
	
	@GetMapping("/popupDeliverySelect")
	public String popupDeliverySelect() {
		log.info("배송지 선택 팝업 호출");
		return "redirect:../Delivery/popupSelect";
	} // end popupDeliverySelect
	
	
}
