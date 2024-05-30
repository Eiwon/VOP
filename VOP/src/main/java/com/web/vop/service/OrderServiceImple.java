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
	
	public Date getExpectDateByPaymentId(int paymentId) { // 배송 예정일 조회 (TEST 중)
		 log.info("getExpectDateByPaymentId()");
		 Date expectDate = orderMapper.selectByExpectDeliveryDate(paymentId);
		 
		 if (expectDate == null) {
	            log.error(expectDate, null);
	            throw new IllegalArgumentException("해당 paymentId에 대한 결제일을 찾을 수 없습니다.");
	        }
		 log.info("배송예상일 : " + expectDate);
		return expectDate;
	}//end getExpectDateByPaymentId

	@Override
	public int getPaymentId(int paymentId) { // 송장 번호 조회
		log.info("getPaymentId()");
		int orderList = orderMapper.selectByPaymentId(paymentId);
		log.info("송장 번호(paymentId) : " + orderList);
		return orderList;
	}

	@Override
	public int registerOrder(OrderVO orderVO) {
		log.info("registerOrder()");
		int res = orderMapper.insertOrder(orderVO);
		log.info(res + "행 추가 성공");
		return res;
	} // end registerOrder
	
	
	@Override
	public List<OrderVO> getOrderByPaymentId(int paymentId) {
		log.info("getOrderByPaymentId()");
		return orderMapper.selectOrderByPaymentId(paymentId);
	} // end getOrderByPaymentId
	
	@Override 
	// 주문 목록 조회 + 이미지 경로 조회 By imgId
	public List<OrderVO> getOrderListByMemberId(String memberId) { 
		log.info("getOrderListByMemberId() - memberId : " + memberId);
		List<OrderVO> orderList = orderMapper.selectOrderListByMemberId(memberId);
		
		if(orderList == null) {
			log.info("주문 내역이 없습니다.");
			return new ArrayList<>(); // 주문 내역이 없으면 빈 리스트 반환
		}
		log.info("주문 조회 : " + orderList);
		return orderList;
	}

	


}// end OrderServiceImple()
