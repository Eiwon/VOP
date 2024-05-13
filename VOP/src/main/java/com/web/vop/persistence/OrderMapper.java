package com.web.vop.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryVO;
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
	
	// 배송지 상세 조회 (받는 사람 , 받는 주소 , 배송 요청 사항 )
	public DeliveryVO selectByMemberId(String memberId);
	
	// 배송지 등록 
	public int insertDelivery(DeliveryVO deliveryVo);
		
	// 배송지 수정 
	public int updateDelivery(DeliveryVO deliveryVo);
	
	// 배송지 삭제
	public int deleteDelivery(String memberId);

}
