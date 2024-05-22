package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.DeliveryVO;

@Mapper
public interface DeliveryMapper {
	
	// 배송지 등록
	int insertDelivery(DeliveryVO deliveryVo);
	
	// 배송지 수정 
	public int updateDelivery(DeliveryVO deliveryVo);
		
	// 배송지 삭제
	public int deleteDelivery(int deliveryId);
	
	// 배송지 상세 조회 (주문조회)
	public List<DeliveryVO> selectByMemberId(String memberId);
	
	// 배송지 상세 조회 (수정페이지) by deliveryId and memberId
	public DeliveryVO selectBydeliveryId(@Param("deliveryId") int deliveryId, @Param("memberId") String memberId);


	// 기본 배송지 개수 조회
	int cntIsDefault(@Param("memberId") String memberId);
	
	// 기존 기본 배송지를 0으로 업데이트
	int updateDefault(String memberId);
	
	// 새로운 기본 배송지를 1로 설정
 	int updateNewDefault(@Param("deliveryId") int deliveryId, @Param("memberId") String memberId);

	// memberId로 기본 배송지 검색
	public DeliveryVO selectDefaultByMemberId(String memberId);

	
}
