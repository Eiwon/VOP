package com.web.vop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.MembershipVO;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.persistence.BasketMapper;
import com.web.vop.persistence.CouponMapper;
import com.web.vop.persistence.CouponPocketMapper;
import com.web.vop.persistence.DeliveryMapper;
import com.web.vop.persistence.MemberMapper;
import com.web.vop.persistence.MembershipMapper;
import com.web.vop.persistence.OrderMapper;
import com.web.vop.persistence.PaymentMapper;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.util.Constant;

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
	
	@Autowired
	CouponPocketMapper couponPocketMapper;
	
	@Autowired
	MembershipMapper membershipMapper;
	
	@Override
	public int getNewPaymentId() {
		log.info("getNewPaymentId()");
		return paymentMapper.selectNextPaymentId();
	} // end getNewPaymentId

	@Override
	public PaymentWrapper makePaymentForm(int[] productIds, int[] productNums, String memberId) {
		PaymentWrapper payment = new PaymentWrapper();
		
		// 유저 정보 등록
		MemberVO memberVO = memberMapper.selectByMemberId(memberId);
		log.info("검색된 유저 정보 : " + memberVO);
		payment.setMemberVO(memberVO);
		
		// 기본 배송지 등록
		DeliveryVO deliveryVO = deliveryMapper.selectDefaultByMemberId(memberId);
		log.info("검색된 배송지 정보 : " + deliveryVO);
		payment.setDeliveryVO(deliveryVO);
		
		
		// 상품 정보 검색 => 주문 정보 형태로 변환
		Map<Integer, Integer> amountMap = new HashMap<>();
		for(int i = 0; i < productIds.length; i++) {
			amountMap.put(productIds[i], productNums[i]);
		}
		List<OrderViewDTO> orderList = productMapper.selectToOrderById(productIds);
		log.info(orderList);
		for(OrderViewDTO order : orderList) {
			order.getOrderVO().setPurchaseNum(amountMap.get(order.getOrderVO().getProductId()));
		}
		payment.setOrderList(orderList);
		
		// 멤버십 정보 등록
		MembershipVO membershipVO = membershipMapper.selectValidByMemberId(memberId);
		payment.setMembershipVO(membershipVO);
		log.info(membershipVO);
		
		return payment;
	} // end makePaymentForm
	
	
	@Transactional(value = "transactionManager")
	@Override
	public int registerPayment(PaymentWrapper payment) throws DataIntegrityViolationException{
		log.info("registerPayment()");
		
		PaymentVO paymentVO = payment.getPaymentVO();
		MyCouponVO myCouponVO = payment.getMyCouponVO();
		int paymentId = paymentVO.getPaymentId();
		String memberId = paymentVO.getMemberId();
		
		paymentMapper.insertPayment(paymentVO); // 결제 결과 등록
		
		// 주문 목록 등록
		for(OrderViewDTO orderDTO : payment.getOrderList()) {
			OrderVO order = orderDTO.getOrderVO();
			order.setPaymentId(paymentId); // 결제 id 추가
			order.setMemberId(memberId);
			productMapper.updateRemains(order.getProductId(), order.getPurchaseNum() * -1);				
			orderMapper.insertOrder(order);
			// 결제된 상품을 장바구니에서 제거
			basketMapper.deleteFromBasket(order.getProductId(), paymentVO.getMemberId());
		}
		
		// 쿠폰 사용 처리
		if(myCouponVO != null) {
			int couponId = myCouponVO.getCouponId();
			couponPocketMapper.updateIsUsed(couponId, memberId, Constant.IS_USED);
		}
		
		return paymentId;
	} // end registerPayment


	@Override
	public PaymentWrapper getPayment(String memberId, int paymentId) {
		log.info("getPayment");
		PaymentWrapper paymentWrapper = new PaymentWrapper();
		PaymentVO paymentVO = paymentMapper.selectByMemberIdAndPaymentId(memberId, paymentId);
		if(paymentVO == null) { // 결과가 없음 = 결제 실패 or url로 비정상 접근
			return null;
		}
		paymentWrapper.setPaymentVO(paymentVO); 
		paymentWrapper.setOrderList(orderMapper.selectOrderByPaymentId(paymentId));
		return paymentWrapper;
	} // end getPayment
	
	
	// 배송조회 
	@Override
	public List<PaymentVO> getPaymentByPaymentId(int paymentId) {
		log.info("getPaymentByPaymentId()");
		List<PaymentVO> list = paymentMapper.selectPaymentByPaymentId(paymentId);
		log.info("베송조회 by paymentId : " + list);
		return list;
	}// end getPaymentByPaymentId()

	
	
	
}
