package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.persistence.DeliveryMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class DeliveryServiceImple implements DeliveryService{
	
	@Autowired
	private DeliveryMapper deliveryMapper;
	
	// 배송지 등록
	@Override
	public int registerDelivery(DeliveryVO deliveryVo) {
		log.info("registerDelivery()");
		try {
			int res = deliveryMapper.insertDelivery(deliveryVo);			
			return res;
		} catch (Exception e) {
			log.error("배송지 등록 중 오류 발생: " + e.getMessage());
			
	        // 사용자에게 오류 페이지를 보여주기
	        throw new RuntimeException("배송지 등록 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
		}
	} // end registerDelivery
	
	// 배송지 수정
	@Override
	public int updateDelivery(DeliveryVO deliveryVo) {
		log.info("updateDelivery()");
		int res = deliveryMapper.updateDelivery(deliveryVo);
		log.info(res + "행 수정");
		return res;
	}// end updateDelivery()
	

	// 배송지 삭제
	@Override
	public int deleteDelivery(int deliveryId) {
		log.info("deleteDelivery()");
		int res = deliveryMapper.deleteDelivery(deliveryId);
		log.info(res + "행 삭제");
		return res;
	}//end deleteDelivery()

	
	// 배송지 상세 조회 by memberId
	@Override
	public List<DeliveryVO> getMemberId(String memberId) {
		log.info("registerDelivery()-memberId : " + memberId);
		List<DeliveryVO> result = deliveryMapper.selectByMemberId(memberId);
		log.info("배송지 상세 조회 : " + result);
		return result;
	}//end getMemberId()

	
	// 배송지 상세 조회 by deliveryId and memberId
	@Override
	public DeliveryVO getDeliveryById(int deliveryId, String memberId) {
		log.info("getDeliveryById()");
		DeliveryVO result = deliveryMapper.selectBydeliveryId(deliveryId, memberId);
		log.info("getDeliveryById : " + result);
		return result;
	}//end getDeliveryById()

	@Override // 기본 배송지 검색
	public DeliveryVO getDefaultDelivery(String memberId) {
		log.info("getDefaultDelivery()");
		DeliveryVO result = deliveryMapper.selectDefaultByMemberId(memberId);
		return result;
	} // end getDefaultDelivery

	// 기본 배송지 카운트
	@Override
	public boolean hasDefaultAddress(String memberId) {
		log.info("hasDefaultAddress()");
		int count = deliveryMapper.cntIsDefault(memberId);
		log.info("기본 배송지 개수: " + count);
		return  count > 0;
	}//end hasDefaultAddress()
	
	//등록하는 deliveryId의 기본배송지를 1로 바꾸기
	@Override
	public int updateNewDefault(int deliveryId, String memberId) {
		log.info("updateNewDefault() - deliveryId: " +  deliveryId);
		log.info("updateNewDefault() - memberId: " + memberId);
		int res = deliveryMapper.updateNewDefault(deliveryId, memberId);
		log.info(res + "행 기본배송지 등록 완료");
		return res;
	}//end updateNewDefault

	
	//해당하는 memberid의 나머지 기본배송지 목록을 0으로 바꾸기
	@Override
	public int updateDefault(String memberId) {
		log.info("updateDefault() - memberId : " + memberId);
		int res = deliveryMapper.updateDefault(memberId);
		log.info(res + "행 기본배송지 해지 완료");
		return res;
	} //end updateNewDefault

	
	
	


	

	
}
