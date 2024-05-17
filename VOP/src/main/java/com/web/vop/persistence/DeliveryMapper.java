package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryVO;

@Mapper
public interface DeliveryMapper {
	
	// 배송지 등록
	int insertDelivery(DeliveryVO deliveryVo);
	
	// 배송지 수정 
	public int updateDelivery(DeliveryVO deliveryVo);
		
	// 배송지 삭제
	public int deleteDelivery(String memberId);
	
	// 배송지 상세 조회 (받는 사람 , 받는 주소 , 배송 요청 사항 )
	public List<DeliveryVO> selectByMemberId(String memberId);
	
	// 배송지 상세 조회 by deliveryId
	public DeliveryVO selectBydeliveryId(int deliveryId);
}
