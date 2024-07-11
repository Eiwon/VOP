package com.web.vop.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;
import com.web.vop.persistence.OrderMapper;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class OrderServiceImple implements OrderService {
	
	@Autowired
	OrderMapper orderMapper;
	
	public String getExpectDateByPaymentId(int paymentId) { // ��� ������ ��ȸ (TEST ��)
		 log.info("getExpectDateByPaymentId()");
		 String expectDate = orderMapper.selectByExpectDeliveryDate(paymentId);
		 
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
	public List<OrderViewDTO> getOrderByPaymentId(int paymentId) {
		log.info("getOrderByPaymentId()");
		return orderMapper.selectOrderByPaymentId(paymentId);
	} // end getOrderByPaymentId
	
	@Override 
	// �ֹ� ��� ��ȸ + �̹��� ��� ��ȸ By imgId
	public List<OrderViewDTO> getOrderListByMemberId(String memberId) { 
		log.info("getOrderListByMemberId() - memberId : " + memberId);
		List<OrderViewDTO> orderList = orderMapper.selectOrderListByMemberId(memberId);
		
		if(orderList == null) {
			log.info("�ֹ� ������ �����ϴ�.");
			return new ArrayList<>(); // �ֹ� ������ ������ �� ����Ʈ ��ȯ
		}
		
		log.info("�ֹ� ��ȸ : " + orderList);
		return orderList;
	}//end getOrderListByMemberId()

	
	// �ֹ� ��� ����
	@Override
	public int deleteOrderListByOrderId(int orderId) {
		log.info("deleteOrderListByOrderId()");
		log.info("orderId : " + orderId);
		
		int res = orderMapper.deleteOrderListByOrderId(orderId);
		log.info("res - " + res);
		return res;
	}//end deleteOrderListByOrderId()

	// �ֹ� ��� (����¡)
	/*
	 * @Override public List<OrderViewDTO> search(PageMaker pageMaker) {
	 * log.info("search()"); Pagination pagination = pageMaker.getPagination();
	 * pageMaker.setTotalCount(orderMapper.selectByOrderlistCnt(pagination)); return
	 * orderMapper.selectByOrderlist(pagination); }// end selectByOrderlist()
	 */	


}// end OrderServiceImple()
