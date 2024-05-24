package com.web.vop.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.OrderVO;


@Mapper
public interface OrderMapper {
	// 배송지 상세 조회 (배송 예정일)
	public Date selectByExpectDeliveryDate(int paymentId); 
	
	// 배송지 상세 조회 (송장 번호)
	public int selectByPaymentId(int paymentId); 
	
	// 주문 등록
	public int insertOrder(OrderVO orderVO);
	
	// paymentId로 주문 검색
	public List<OrderVO> selectOrderByPaymentId(int paymentId);
	
	// 주문 목록 조회 
	public List<OrderVO> selectOrderListByMemberId(String memberId);

}
