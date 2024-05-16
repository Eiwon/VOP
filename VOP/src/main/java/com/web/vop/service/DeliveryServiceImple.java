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
	
	// ����� ���
	@Override
	public int registerDelivery(DeliveryVO deliveryVO) {
		log.info("registerDelivery()");
		int res = deliveryMapper.insertDelivery(deliveryVO);
		return res;
	} // end registerDelivery
	
	// ����� ����
	@Override
	public int updateDelivery(DeliveryVO deliveryVo) {
		log.info("updateDelivery()");
		int res = deliveryMapper.updateDelivery(deliveryVo);
		log.info(res + "�� ����");
		return res;
	}// end updateDelivery()
	
	// ����� ����
	@Override
	public int deleteDelivery(String memberId) {
		log.info("deleteDelivery()");
		int res = deliveryMapper.deleteDelivery(memberId);
		log.info(res + "�� ����");
		return res;
	}//end deleteDelivery()
	
	// ����� �� ��ȸ
	@Override
	public List<DeliveryVO> getMemberId(String memberId) {
		log.info("registerDelivery()-memberId : " + memberId);
		List<DeliveryVO> result = deliveryMapper.selectByMemberId(memberId);
		log.info("����� �� ��ȸ : " + result.toString());
		return result;
	}//end getMemberId()
	
	
}
