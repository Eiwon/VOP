package com.web.vop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.domain.ProductVO;
import com.web.vop.persistence.BasketMapper;
import com.web.vop.persistence.CouponMapper;
import com.web.vop.persistence.CouponPocketMapper;
import com.web.vop.persistence.DeliveryMapper;
import com.web.vop.persistence.MemberMapper;
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
	
	@Override
	public int getNewPaymentId() {
		log.info("getNewPaymentId()");
		return paymentMapper.selectNextPaymentId();
	} // end getNewPaymentId

	@Override
	public PaymentWrapper makePaymentForm(int[] productIds, int[] productNums, String memberId) {
		PaymentWrapper payment = new PaymentWrapper();
		
		// ���� ���� ���
		MemberVO memberVO = memberMapper.selectByMemberId(memberId);
		log.info("�˻��� ���� ���� : " + memberVO);
		payment.setMemberVO(memberVO);
		
		// �⺻ ����� ���
		DeliveryVO deliveryVO = deliveryMapper.selectDefaultByMemberId(memberId);
		log.info("�˻��� ����� ���� : " + deliveryVO);
		payment.setDeliveryVO(deliveryVO);
		
		
		List<OrderVO> orderList = new ArrayList<>();
		// ��ǰ ���� �˻� => �ֹ� ���� ���·� ��ȯ
		for(int i = 0; i < productIds.length; i++) {
			ProductVO productVO = productMapper.selectProduct(productIds[i]);
			orderList.add(new OrderVO(
							0, 0, productVO.getProductId(), productVO.getProductName(), productVO.getProductPrice(), 
							productNums[i], null, productVO.getImgId(), memberId)
							);
		} // end for
		payment.setOrderList(orderList);
		
		
		return payment;
	} // end makePaymentForm
	
	
	@Transactional(value = "transactionManager")
	@Override
	public int registerPayment(PaymentWrapper payment) {
		log.info("registerPayment()");
		int res = 0;
		PaymentVO paymentVO = payment.getPaymentVO();
		MyCouponVO myCouponVO = payment.getMyCouponVO();
		int paymentId = paymentVO.getPaymentId();
		List<OrderVO> orderList = payment.getOrderList();
		
		// �ֹ� ���� ��ȿ�� �˻�
		for(int i = 0; i < orderList.size(); i++) {
			if(orderList.get(i).getPurchaseNum() > productMapper.selectRemainsById(orderList.get(i).getProductId())) {
				return i * -1 - 1;
			}
		}
		
		// �ֹ� ��� ���
		for(OrderVO order : payment.getOrderList()) {
			order.setPaymentId(paymentId); // ���� id �߰�
			productMapper.updateRemains(order.getProductId(), order.getPurchaseNum());				
			orderMapper.insertOrder(order);
			// ������ ��ǰ�� ��ٱ��Ͽ��� ����
			basketMapper.deleteFromBasket(order.getProductId(), paymentVO.getMemberId());
		}
		
		// ���� ��� ó��
		if(myCouponVO != null) {
			int couponId = myCouponVO.getCouponId();
			String memberId = paymentVO.getMemberId();
			couponPocketMapper.updateIsUsed(couponId, memberId, Constant.IS_USED);
		}
		
		res = paymentMapper.insertPayment(paymentVO); // ���� ��� ���
		
		return res;
	} // end registerPayment

//	@Override
//	public PaymentWrapper getRecentPayment(String memberId) {
//		log.info("getRecentPayment()");
//		PaymentWrapper payment = new PaymentWrapper();
//		payment.setPaymentVO(paymentMapper.selectLastPayment(memberId)); 
//		payment.setOrderList(orderMapper.selectOrderByPaymentId(payment.getPaymentVO().getPaymentId()));
//		
//		return payment; 
//	} // end getRecentPayment

	@Override
	public PaymentWrapper getPayment(String memberId, int paymentId) {
		log.info("getPayment");
		PaymentWrapper paymentWrapper = new PaymentWrapper();
		PaymentVO paymentVO = paymentMapper.selectByMemberIdAndPaymentId(memberId, paymentId);
		if(paymentVO == null) { // ����� ���� = ���� ���� or url�� ������ ����
			return null;
		}
		paymentWrapper.setPaymentVO(paymentVO); 
		paymentWrapper.setOrderList(orderMapper.selectOrderByPaymentId(paymentId));
		return paymentWrapper;
	} // end getPayment
	
	
	// �����ȸ 
	@Override
	public List<PaymentVO> getPaymentByPaymentId(int paymentId) {
		log.info("getPaymentByPaymentId()");
		List<PaymentVO> list = paymentMapper.selectPaymentByPaymentId(paymentId);
		log.info("������ȸ by paymentId : " + list);
		return list;
	}// end getPaymentByPaymentId()

	
	
	
}
