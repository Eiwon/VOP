package com.web.vop.persistence;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryExpectDTO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;



@Mapper
public interface OrderMapper {
	// 배송지 상세 조회 (배송 예정일)
	public String selectByExpectDeliveryDate(int orderId); 
	
	// 배송지 상세 조회 (송장 번호)
	public int selectByPaymentId(int paymentId); 
	
	// 주문 등록
	public int insertOrder(OrderVO orderVO);
	
	// paymentId로 주문 검색
	public List<OrderViewDTO> selectOrderByPaymentId(int paymentId);
	
	// 주문 목록 조회 
	public List<OrderViewDTO> selectOrderListByMemberId(String memberId);

	// 주문 목록 삭제
	public int deleteOrderListByOrderId(int orderId);
	
	// 배송 예정일 조회 스케줄링
	 List<DeliveryExpectDTO> selectDeliveryExpect();
	
	// 상품 목록 조회 (페이징)
	 //List<OrderViewDTO> selectByOrderlist(Pagination pagination);
	 
	// 상품 목록 수 (페이징)
	 //int selectByOrderlistCnt(Pagination pagination);
}
