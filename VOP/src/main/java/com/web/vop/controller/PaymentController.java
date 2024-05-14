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
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.domain.ProductVO;
import com.web.vop.service.BasketService;
import com.web.vop.service.CouponService;
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
	private MemberService memberService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BasketService basketService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/checkout")
	public void makeOrders(Model model, int[] productIds, int[] productNums, String memberId) {
		log.info("makeOrders() - memberId : " + memberId);
		log.info("productIds length : " + productIds.length);
		log.info("first productId : " + productIds[0] + ", first productNums : " + productNums[0]);
		
		List<OrderVO> orderList = new ArrayList<>();
		
		// 유저 정보 검색
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		
		// 배송지 정보 (미구현)
		
		
		// 상품 정보 검색 => 주문 정보 형태로 변환
		for(int i = 0; i < productIds.length; i++) {
			ProductVO productVO = productService.getProductById(productIds[i]);
			orderList.add(new OrderVO(
					0, 0, productVO.getProductId(), productVO.getProductName(), productVO.getProductPrice(), 
					productNums[i], null, productVO.getImgId(), memberId)
					);
		}
		
		try { // 자바스크립트에서 쓰기 위해 json 형식 문자열로 변환
			model.addAttribute("orderList", new ObjectMapper().writeValueAsString(orderList));
			model.addAttribute("memberVO", new ObjectMapper().writeValueAsString(memberVO));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} // end toCheckout
	
	
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
		log.info("결제 결과 저장");
		
		PaymentVO paymentVO = paymentResult.getPaymentVO();
		List<OrderVO> orderList = paymentResult.getOrderList();
		CouponVO couponVO = paymentResult.getCouponVO();
		
		log.info(paymentVO);
		log.info(orderList);
		log.info(couponVO);
		
		int res = paymentService.registerPayment(paymentVO); // 결제 결과 등록
		int paymentId = paymentVO.getPaymentId();
		if(res == 1) { // 각 주문 목록 등록
			int totalRes = 0;
			for(OrderVO order : orderList) {
				order.setPaymentId(paymentId); // 결제 id 추가
				totalRes += orderService.registerOrder(order);
				// 결제된 상품을 장바구니에서 제거
				basketService.removeFromBasket(order.getProductId(), paymentVO.getMemberId());
			}
			log.info("총 " + totalRes + "행 추가 성공");
		}
		// 쿠폰 사용 처리
		if(couponVO != null) {
			int couponNum = couponVO.getCouponNum() -1;
			if(couponNum > 0) { // 쿠폰 사용 후, 쿠폰이 남아있다면 갯수 변경
				couponVO.setCouponNum(couponNum);
				couponService.setCouponNum(couponVO);
			}else { // 더 이상 남은 쿠폰이 없으면 삭제
				couponService.removeCoupon(couponVO);
			}
		}
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end savePaymentResult
	
	@GetMapping("/payment")
	@ResponseBody
	public ResponseEntity<PaymentWrapper> sendPaymentResult(HttpServletRequest request){
		log.info("결제 결과 조회 요청");
		String memberId = (String) request.getSession().getAttribute("memberId");
		PaymentWrapper payment = new PaymentWrapper(); // 각 주문정보와 전체 결제 내역을 한번에 보내기 위해 포장
		payment.setPaymentVO(
				paymentService.getRecentPayment(memberId)
				);
		payment.setOrderList(
				orderService.getOrderByPaymentId(payment.getPaymentVO().getPaymentId())
				);
		
		return new ResponseEntity<PaymentWrapper>(payment, HttpStatus.OK);
	} // end sendPaymentResult
	
	@GetMapping("/coupon")
	@ResponseBody
	public ResponseEntity<List<CouponVO>> getCouponList(HttpServletRequest request){
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("쿠폰 리스트 요청 : " + memberId);
		
		List<CouponVO> result = couponService.getByMemberId(memberId);
		log.info(result.size() + "개 쿠폰 검색");
		return new ResponseEntity<List<CouponVO>>(result, HttpStatus.OK);
	} // end getCouponList
	
	
}
