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
	public int registerDelivery(DeliveryVO deliveryVo) {
		log.info("registerDelivery()");
		try {
			int res = deliveryMapper.insertDelivery(deliveryVo);			
			return res;
		} catch (Exception e) {
			log.error("����� ��� �� ���� �߻�: " + e.getMessage());
			
	        // ����ڿ��� ���� �������� �����ֱ�
	        throw new RuntimeException("����� ��� �� ������ �߻��߽��ϴ�. ��� �� �ٽ� �õ����ּ���.");
		}
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
	
	// ����� �� ��ȸ by memberId
	@Override
	public List<DeliveryVO> getMemberId(String memberId) {
		log.info("registerDelivery()-memberId : " + memberId);
		List<DeliveryVO> result = deliveryMapper.selectByMemberId(memberId);
		log.info("����� �� ��ȸ : " + result.toString());
		return result;
	}//end getMemberId()

	// ����� �� ��ȸ by deliveryId 
	@Override
	public DeliveryVO getDeliveryById(int deliveryId) {
		log.info("getDeliveryById()-deliveryId : " + deliveryId);
		DeliveryVO result = deliveryMapper.selectBydeliveryId(deliveryId);
		log.info("getDeliveryById : " + result);
		return result;
	}//end getDeliveryById()
	
	
}