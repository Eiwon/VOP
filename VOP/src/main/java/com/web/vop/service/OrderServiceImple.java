package com.web.vop.service;

import java.util.ArrayList;
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
		 Date orderList = orderMapper.selectByExpectDeliveryDate(paymentId);
		 log.info("배송 예정일 : " + orderList);
		return orderList;
	}

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
	public List<DeliveryVO> getMemberId(String memberId) { // 배송지 상세 조회
		log.info("registerDelivery()-memberId : " + memberId);
		List<DeliveryVO> result = orderMapper.selectByMemberId(memberId);
		log.info("배송지 상세 조회 : " + result.toString());
		return result;
	}
	
	@Override
	public int registerDelivery(DeliveryVO deliveryVo) { // 배송지 등록 
		log.info("registerDelivery()");
		int res = orderMapper.insertDelivery(deliveryVo);
		log.info(res + "행 삽입");
		return res;
	}

	@Override
	public int updateDelivery(DeliveryVO deliveryVo) { // 배송지 수정 
		log.info("updateDelivery()");
		int res = orderMapper.updateDelivery(deliveryVo);
		log.info(res + "행 수정");
		return res;
	}

	@Override
	public int deleteDelivery(String memberId) { // 배송지 삭제
		log.info("deleteDelivery()");
		int res = orderMapper.deleteDelivery(memberId);
		log.info(res + "행 삭제");
		return res;
	}

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
