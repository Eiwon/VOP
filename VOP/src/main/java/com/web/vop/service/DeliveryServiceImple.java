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
		log.info("배송지 상세 조회 : " + result.toString());
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

	
	// 기본 배송지 조회
	@Override
	public boolean hasDefaultAddress(String memberId) {
		log.info("hasDefaultAddress()");
		int count = deliveryMapper.cntIsDefault(memberId);
		log.info("기본 배송지 개수: " + count);
		return  count > 0;
	}//end hasDefaultAddress()

	
	// 기본 배송지 설정하기
	@Override
	public void setDefaultDelivery(int deliveryId, String memberId) {
		log.info("setDefaultDelivery() with deliveryId: {}" + deliveryId);
		log.info("setDefaultDelivery() with memberId: {}" + memberId);
		
		  try {
		        // 기존 기본 배송지를 0으로 업데이트
		        int updateDefaultResult = deliveryMapper.updateDefault(memberId);
		        log.info("기존의 기본 배송지를 0으로 업데이트 : " + updateDefaultResult + "행 수정");
		        
		        // 새로운 기본 배송지를 1로 설정
		        int updateNewDefaultResult = deliveryMapper.updateNewDefault(deliveryId, memberId);
		        log.info("새로운 배송지를 기본 배송지로 설정: " + updateNewDefaultResult + "행 수정");
		    } catch (Exception e) {
		        log.error(e);
		        throw e; // 재throw 하여 상위 레이어에서도 예외를 처리할 수 있도록 
		    }
	}//end setDefaultDelivery()


	

	
}
