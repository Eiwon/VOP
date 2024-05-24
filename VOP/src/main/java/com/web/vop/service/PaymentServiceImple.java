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
import com.web.vop.persistence.BasketMapper;
import com.web.vop.persistence.CouponMapper;
import com.web.vop.persistence.DeliveryMapper;
import com.web.vop.persistence.MemberMapper;
import com.web.vop.persistence.OrderMapper;
import com.web.vop.persistence.PaymentMapper;
import com.web.vop.persistence.ProductMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class PaymentServiceImple implements PaymentService {
	
	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	DeliveryMapper deliveryMapper;
	
	@Autowired
	PaymentMapper paymentMapper;
	
	@Autowired
	OrderMapper orderMapper;

	@Autowired
	ProductMapper productMapper;
	
	@Autowired
	BasketMapper basketMapper;
	
	@Autowired
	CouponMapper couponMapper;
	
	@Override
	public int getNewPaymentId() {
		log.info("getNewPaymentId()");
		return paymentMapper.selectNextPaymentId();
	} // end getNewPaymentId

	@Override
	public PaymentWrapper makePaymentForm(int[] productIds, int[] productNums, String memberId) {
		PaymentWrapper payment = new PaymentWrapper();
		// 유저 정보 검색
		MemberVO memberVO = memberMapper.selectByMemberId(memberId);
		log.info("검색된 유저 정보 : " + memberVO);
		
		// 배송지 정보
		DeliveryVO deliveryVO = deliveryMapper.selectDefaultByMemberId(memberId);
		log.info("검색된 배송지 정보 : " + deliveryVO);
		
		List<OrderVO> orderList = new ArrayList<>();
		// 상품 정보 검색 => 주문 정보 형태로 변환
		for(int i = 0; i < productIds.length; i++) {
			ProductVO productVO = productMapper.selectProduct(productIds[i]);
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
			orderMapper.insertOrder(order);
			// 결제된 상품을 장바구니에서 제거
			res = basketMapper.deleteFromBasket(order.getProductId(), paymentVO.getMemberId());
		}
		
		// 쿠폰 사용 처리
		if(couponVO != null) {
			int couponNum = couponMapper.selectCouponNum(couponVO); // 현재 쿠폰 수 조회
			if(couponNum - 1 > 0) { // 사용 후, 쿠폰이 남아있다면 갯수 변경
				couponVO.setCouponNum(couponNum -1);
				res = couponMapper.updateCouponNum(couponVO);
			}else { // 더 이상 남은 쿠폰이 없으면 삭제
				res = couponMapper.deleteCouponSelected(couponVO);
			}
		}
		
		return res;
	} // end registerPayment

	@Override
	public PaymentWrapper getRecentPayment(String memberId) {
		log.info("getRecentPayment()");
		PaymentWrapper payment = new PaymentWrapper();
		payment.setPaymentVO(paymentMapper.selectLastPayment(memberId)); 
		payment.setOrderList(orderMapper.selectOrderByPaymentId(payment.getPaymentVO().getPaymentId()));
		
		return payment; 
	} // end getRecentPayment
	
	
	
}
