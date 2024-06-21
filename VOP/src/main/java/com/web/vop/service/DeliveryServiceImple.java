package com.web.vop.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.DeliveryListDTO;
import com.web.vop.domain.DeliveryVO;
import com.web.vop.persistence.DeliveryMapper;
import com.web.vop.util.Constant;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class DeliveryServiceImple implements DeliveryService{
	
	@Autowired
	private DeliveryMapper deliveryMapper;
	
	// 배송지 등록
	@Transactional(value = "transactionManager")
	@Override
	public int registerDelivery(DeliveryVO deliveryVo) {
		log.info("registerDelivery()");
		
		if(deliveryVo.getIsDefault() == Constant.DELIVERY_DEFAULT) { // 등록할 배송지가 기본 배송지이면, 기존 기본 배송지를 0으로 변경
			deliveryMapper.updateDefault(deliveryVo.getMemberId());
		}
		int res = deliveryMapper.insertDelivery(deliveryVo);
		return res;
	} // end registerDelivery
	
	// 배송지 수정
	@Transactional(value = "transactionManager")
	@Override
	public int updateDelivery(DeliveryVO deliveryVo) {
		log.info("updateDelivery()");
		
		if(deliveryVo.getIsDefault() == 1) { // 변경할 배송지가 기본 배송지로 설정되었으면, 기존 기본 배송지를 0으로 변경
			deliveryMapper.updateDefault(deliveryVo.getMemberId());
		}
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

	
	// 배송지 조회
	@Override
	public List<DeliveryListDTO> getDeliveryList(int paymentId) {
		log.info("getDeliveryList() - paymentId : " + paymentId);
		List<DeliveryListDTO> list = deliveryMapper.selectDeliveryList(paymentId);
		
		//리스트가 비어있는지 아닌지 확인 
		if(list == null || list.isEmpty()) {
			 log.info("DeliveryList is empty or null");
		}else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (DeliveryListDTO dto : list) {
				log.info("배송번호 : " + dto.getPaymentId());
				log.info("받는 사람 : " + dto.getReceiverName());
				log.info("주소 : " + dto.getDeliveryAddress());
				log.info("배송요구사항 : " + dto.getRequirement());
				
				 // 날짜 포맷팅 적용
                Date expectDeliveryDate = dto.getExpectDeliveryDate();
                String formattedDate = dateFormat.format(expectDeliveryDate);
                
                log.info("배송예정일 : " + formattedDate);
				log.info("배송예정일 : " + dto.getExpectDeliveryDate());
				
				// DTO에 포맷팅된 날짜 설정
                dto.setFormattedExpectDeliveryDate(formattedDate);
			}
		}
		
		return list;
	}//end getDeliveryList()

	
	
	


	

	
}
