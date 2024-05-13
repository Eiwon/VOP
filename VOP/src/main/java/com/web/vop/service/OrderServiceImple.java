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
	public Date getExpectDateByPaymentId(int paymentId) { // 배송 예정일 조회
		 log.info("getExpectDateByPaymentId()");
		 Date result = orderMapper.selectByExpectDeliveryDate(paymentId);
		 log.info("배송 예정일 : " + result);
		return result;
	}

	@Override
	public int getPaymentId(int paymentId) { // 송장 번호 조회
		log.info("getPaymentId()");
		int result = orderMapper.selectByPaymentId(paymentId);
		log.info("송장 번호(paymentId) : " + result);
		return result;
	}

	@Override
	public int registerOrder(OrderVO orderVO) {
		log.info("registerOrder()");
		int res = orderMapper.insertOrder(orderVO);
		log.info(res + "행 추가 성공");
		return res;
	} // end registerOrder
	
	@Override
	public DeliveryVO getMemberId(String memberId) { // 배송지 상세 조회 (받는 사람 , 받는 주소 , 배송 요청 사항 )
		log.info("getMemberId()");
		DeliveryVO result = orderMapper.selectByMemberId(memberId);
		log.info("배송지 상세 조회 : " + result);
		return result;
	}

	
	@Override
	public int registerDelivery(DeliveryVO deliveryVo) { // 배송지 등록 
		log.info("registerDelivery()");
		int result = orderMapper.insertDelivery(deliveryVo);
		log.info(result + "행 삽입");
		return result;
	}

	@Override
	public int updateDelivery(DeliveryVO deliveryVo) { // 배송지 수정 
		log.info("updateDelivery()");
		int result = orderMapper.updateDelivery(deliveryVo);
		log.info(result + "행 수정");
		return result;
	}

	@Override
	public int deleteDelivery(String memberId) { // 배송지 삭제
		log.info("deleteDelivery()");
		int result = orderMapper.deleteDelivery(memberId);
		log.info(result + "행 삭제");
		return result;
	}

	@Override
	public List<OrderVO> getOrderByPaymentId(int paymentId) {
		log.info("getOrderByPaymentId()");
		return orderMapper.selectOrderByPaymentId(paymentId);
	} // end getOrderByPaymentId


}
