package com.web.vop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.CouponVO;
import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.domain.ProductVO;
import com.web.vop.persistence.PaymentMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class PaymentServiceImple implements PaymentService {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	DeliveryService deliverService;
	
	@Autowired
	PaymentMapper paymentMapper;
	
	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;
	
	@Autowired
	BasketService basketService;
	
	@Autowired
	CouponService couponService;
	
	@Override
	public int getNewPaymentId() {
		log.info("getNewPaymentId()");
		return paymentMapper.selectNextPaymentId();
	} // end getNewPaymentId

	@Override
	public PaymentWrapper makePaymentForm(int[] productIds, int[] productNums, String memberId) {
		PaymentWrapper payment = new PaymentWrapper();
		// 유저 정보 검색
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		log.info("검색된 유저 정보 : " + memberVO);
		
		// 배송지 정보 (미구현)
		DeliveryVO deliveryVO = deliverService.getDefaultDelivery(memberId);
		log.info("검색된 배송지 정보 : " + deliveryVO);
		
		List<OrderVO> orderList = new ArrayList<>();
		// 상품 정보 검색 => 주문 정보 형태로 변환
		for(int i = 0; i < productIds.length; i++) {
			ProductVO productVO = productService.getProductById(productIds[i]);
			orderList.add(new OrderVO(
							0, 0, productVO.getProductId(), productVO.getProductName(), productVO.getProductPrice(), 
							productNums[i], null, productVO.getImgId(), memberId)
							);
		} // end for
		
		payment.setMemberVO(memberVO);
		payment.setDeliveryVO(deliveryVO);
		payment.setOrderList(orderList);
		
		return payment;
	} // end makePaymentForm
	
	
	@Transactional(value = "transactionManager")
	@Override
	public int registerPayment(PaymentWrapper payment) {
		log.info("registerPayment()");
		int res = 0;
		paymentMapper.insertPayment(payment.getPaymentVO()); // 결제 결과 등록
		PaymentVO paymentVO = payment.getPaymentVO();
		CouponVO couponVO = payment.getCouponVO();
		int paymentId = paymentVO.getPaymentId();
		
		// 주문 목록 등록
		for(OrderVO order : payment.getOrderList()) {
			order.setPaymentId(paymentId); // 결제 id 추가
			res = orderService.registerOrder(order);
			// 결제된 상품을 장바구니에서 제거
			basketService.removeFromBasket(order.getProductId(), paymentVO.getMemberId());
		}
		
		// 쿠폰 사용 처리
		if(couponVO != null) {
			couponService.useUpCoupon(couponVO);
		}
		
		return res;
	} // end registerPayment

	@Override
	public PaymentWrapper getRecentPayment(String memberId) {
		log.info("getRecentPayment()");
		PaymentWrapper payment = new PaymentWrapper();
		payment.setPaymentVO(paymentMapper.selectLastPayment(memberId)); 
		payment.setOrderList(orderService.getOrderByPaymentId(payment.getPaymentVO().getPaymentId()));
		
		return payment; 
	} // end getRecentPayment
	
	
	
}
