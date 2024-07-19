package com.web.vop.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.OrderViewDTO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.service.AWSS3Service;
import com.web.vop.service.PaymentService;
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
	
	@Autowired
	private AWSS3Service awsS3Service;
	
	@PostMapping("/checkout")
	public void makePayment(
			Model model, int[] productIds, int[] productNums, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("makePayment()");
		log.info("결제할 상품 갯수 : " + productIds.length);
		
		PaymentWrapper payment = paymentService.makePaymentForm(productIds, productNums, memberDetails.getUsername());
		
		for(OrderViewDTO order : payment.getOrderList()) {
			order.setImgUrl(awsS3Service.toImageUrl(order.getImgPath(), order.getImgChangeName()));
		}
		log.info("결제 정보 : " + payment);
		model.addAttribute("paymentWrapper", payment);
	} // end makePayment
	
	
	@GetMapping("/paymentResult")
	public void paymentResultGET(Model model, int paymentId , @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("paymentResult.jsp 이동 요청");
		String memberId = memberDetails.getUsername();
		// 각 주문정보와 전체 결제 내역을 한번에 보내기 위해 포장
		PaymentWrapper paymentWrapper = paymentService.getPayment(memberId, paymentId);
		
		for(OrderViewDTO order : paymentWrapper.getOrderList()) {
			order.setImgUrl(awsS3Service.toImageUrl(order.getImgPath(), order.getImgChangeName()));
		}
		
		model.addAttribute("paymentWrapper", paymentWrapper);
		
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
	public ResponseEntity<Integer> savePaymentResult(@RequestBody PaymentWrapper paymentResult, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("---------결제 결과 저장--------");
		log.info("결제 내역 : " + paymentResult.getPaymentVO());
		log.info("주문 목록 : " + paymentResult.getOrderList());
		log.info("쿠폰 사용 내역 : " + paymentResult.getMyCouponVO());
		log.info("멤버십 정보 : " + paymentResult.getMembershipVO());
		PaymentVO paymentVO = paymentResult.getPaymentVO();
		String impUid = paymentVO.getChargeId();
		String memberId = memberDetails.getUsername();

		int resultCode = checkPaymentValid(paymentResult, memberId);
		
		if(resultCode != 100) {
			paymentAPIUtil.cancelPayment(impUid); // 결제 취소
			return new ResponseEntity<Integer>(resultCode, HttpStatus.OK);
		}
		
		paymentResult.getPaymentVO().setMemberId(memberId);
		try {
			paymentService.registerPayment(paymentResult); // 결제 결과 등록
		}catch(DataIntegrityViolationException e) {
			log.error("DB 저장 실패 : 재고 부족");
			paymentAPIUtil.cancelPayment(impUid); // 결제 취소
			resultCode = 205;
		}
		
		return new ResponseEntity<Integer>(resultCode, HttpStatus.OK);
	} // end savePaymentResult
	
	@GetMapping("/popupDeliverySelect")
	public String popupDeliverySelect() {
		log.info("배송지 선택 팝업 호출");
		return "redirect:../Delivery/popupSelect";
	} // end popupDeliverySelect
	
	private int checkPaymentValid(PaymentWrapper paymentResult, String memberId) {
		log.info("결제 결과 유효성 검사");
		PaymentVO paymentVO = paymentResult.getPaymentVO();
		String impUid = paymentVO.getChargeId();
		int chargePrice = paymentVO.getChargePrice();
		
		// 주문 목록 총액이 결제금액과 일치하는지 확인
		int total = 0;
		for(OrderViewDTO orderDTO : paymentResult.getOrderList()) {
			total += orderDTO.getOrderVO().getProductPrice() * orderDTO.getOrderVO().getPurchaseNum();
		}
		int totalDiscount = paymentVO.getMembershipDiscount() + paymentVO.getCouponDiscount();
		total = (totalDiscount >= 100) ? 0 : (total + paymentVO.getDeliveryPrice()) * (100 - totalDiscount) / 100;
		
		if(total != chargePrice) {
			log.error("구매물품 총액 " + total + "원 vs " + chargePrice + "원 : 결제액 불일치");
			return 201;
		}
		
		Date now = new Date();
		log.info(paymentResult.getMembershipVO());
		if(paymentResult.getMembershipVO().getExpiryDate() != null) {
			if(paymentResult.getMembershipVO().getExpiryDate().before(now)) {
				log.error("만료된 멤버십 정보");
				return 203;
			}
		}
		
		// 서버로 전달된 금액이 실제 결제 금액과 같은지 확인
		int realChargePrice = paymentAPIUtil.getPaymentAmount(impUid);
		if(realChargePrice != chargePrice) {
			// 결제 에러
			log.error("실결제액 : 서버 전송 결제액 불일치");
			return 202;
		}
		return 100;
	} // end checkPaymentValid
}
