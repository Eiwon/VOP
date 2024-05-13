package com.web.vop.service;

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
	
	@Override
	public Date getExpectDateByPaymentId(int paymentId) { // ��� ������ ��ȸ
		 log.info("getExpectDateByPaymentId()");
		 Date result = orderMapper.selectByExpectDeliveryDate(paymentId);
		 log.info("��� ������ : " + result);
		return result;
	}

	@Override
	public int getPaymentId(int paymentId) { // ���� ��ȣ ��ȸ
		log.info("getPaymentId()");
		int result = orderMapper.selectByPaymentId(paymentId);
		log.info("���� ��ȣ(paymentId) : " + result);
		return result;
	}

	@Override
	public int registerOrder(OrderVO orderVO) {
		log.info("registerOrder()");
		int res = orderMapper.insertOrder(orderVO);
		log.info(res + "�� �߰� ����");
		return res;
	} // end registerOrder
	
	@Override
	public DeliveryVO getMemberId(String memberId) { // ����� �� ��ȸ (�޴� ��� , �޴� �ּ� , ��� ��û ���� )
		log.info("getMemberId()");
		DeliveryVO result = orderMapper.selectByMemberId(memberId);
		log.info("����� �� ��ȸ : " + result);
		return result;
	}

	
	@Override
	public int registerDelivery(DeliveryVO deliveryVo) { // ����� ��� 
		log.info("registerDelivery()");
		int result = orderMapper.insertDelivery(deliveryVo);
		log.info(result + "�� ����");
		return result;
	}

	@Override
	public int updateDelivery(DeliveryVO deliveryVo) { // ����� ���� 
		log.info("updateDelivery()");
		int result = orderMapper.updateDelivery(deliveryVo);
		log.info(result + "�� ����");
		return result;
	}

	@Override
	public int deleteDelivery(String memberId) { // ����� ����
		log.info("deleteDelivery()");
		int result = orderMapper.deleteDelivery(memberId);
		log.info(result + "�� ����");
		return result;
	}

	@Override
	public List<OrderVO> getOrderByPaymentId(int paymentId) {
		log.info("getOrderByPaymentId()");
		return orderMapper.selectOrderByPaymentId(paymentId);
	} // end getOrderByPaymentId


}
