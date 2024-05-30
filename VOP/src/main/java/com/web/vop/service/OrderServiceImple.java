package com.web.vop.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.persistence.OrderMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class OrderServiceImple implements OrderService {
	
	@Autowired
	OrderMapper orderMapper;
	
	public Date getExpectDateByPaymentId(int paymentId) { // ��� ������ ��ȸ (TEST ��)
		 log.info("getExpectDateByPaymentId()");
		 Date expectDate = orderMapper.selectByExpectDeliveryDate(paymentId);
		 
		 if (expectDate == null) {
	            log.error(expectDate, null);
	            throw new IllegalArgumentException("�ش� paymentId�� ���� �������� ã�� �� �����ϴ�.");
	        }
		 log.info("��ۿ����� : " + expectDate);
		return expectDate;
	}//end getExpectDateByPaymentId

	@Override
	public int getPaymentId(int paymentId) { // ���� ��ȣ ��ȸ
		log.info("getPaymentId()");
		int orderList = orderMapper.selectByPaymentId(paymentId);
		log.info("���� ��ȣ(paymentId) : " + orderList);
		return orderList;
	}

	@Override
	public int registerOrder(OrderVO orderVO) {
		log.info("registerOrder()");
		int res = orderMapper.insertOrder(orderVO);
		log.info(res + "�� �߰� ����");
		return res;
	} // end registerOrder
	
	
	@Override
	public List<OrderVO> getOrderByPaymentId(int paymentId) {
		log.info("getOrderByPaymentId()");
		return orderMapper.selectOrderByPaymentId(paymentId);
	} // end getOrderByPaymentId
	
	@Override 
	// �ֹ� ��� ��ȸ + �̹��� ��� ��ȸ By imgId
	public List<OrderVO> getOrderListByMemberId(String memberId) { 
		log.info("getOrderListByMemberId() - memberId : " + memberId);
		List<OrderVO> orderList = orderMapper.selectOrderListByMemberId(memberId);
		
		if(orderList == null) {
			log.info("�ֹ� ������ �����ϴ�.");
			return new ArrayList<>(); // �ֹ� ������ ������ �� ����Ʈ ��ȯ
		}
		log.info("�ֹ� ��ȸ : " + orderList);
		return orderList;
	}

	


}// end OrderServiceImple()
