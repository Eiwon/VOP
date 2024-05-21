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
		// ���� ���� �˻�
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		log.info("�˻��� ���� ���� : " + memberVO);
		
		// ����� ���� (�̱���)
		DeliveryVO deliveryVO = deliverService.getDefaultDelivery(memberId);
		log.info("�˻��� ����� ���� : " + deliveryVO);
		
		List<OrderVO> orderList = new ArrayList<>();
		// ��ǰ ���� �˻� => �ֹ� ���� ���·� ��ȯ
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
		paymentMapper.insertPayment(payment.getPaymentVO()); // ���� ��� ���
		PaymentVO paymentVO = payment.getPaymentVO();
		CouponVO couponVO = payment.getCouponVO();
		int paymentId = paymentVO.getPaymentId();
		
		// �ֹ� ��� ���
		for(OrderVO order : payment.getOrderList()) {
			order.setPaymentId(paymentId); // ���� id �߰�
			res = orderService.registerOrder(order);
			// ������ ��ǰ�� ��ٱ��Ͽ��� ����
			basketService.removeFromBasket(order.getProductId(), paymentVO.getMemberId());
		}
		
		// ���� ��� ó��
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
