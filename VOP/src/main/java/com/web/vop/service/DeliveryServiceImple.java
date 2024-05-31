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
	public int deleteDelivery(int deliveryId) {
		log.info("deleteDelivery()");
		int res = deliveryMapper.deleteDelivery(deliveryId);
		log.info(res + "�� ����");
		return res;
	}//end deleteDelivery()

	
	// ����� �� ��ȸ by memberId
	@Override
	public List<DeliveryVO> getMemberId(String memberId) {
		log.info("registerDelivery()-memberId : " + memberId);
		List<DeliveryVO> result = deliveryMapper.selectByMemberId(memberId);
		log.info("����� �� ��ȸ : " + result);
		return result;
	}//end getMemberId()

	
	// ����� �� ��ȸ by deliveryId and memberId
	@Override
	public DeliveryVO getDeliveryById(int deliveryId, String memberId) {
		log.info("getDeliveryById()");
		DeliveryVO result = deliveryMapper.selectBydeliveryId(deliveryId, memberId);
		log.info("getDeliveryById : " + result);
		return result;
	}//end getDeliveryById()

	@Override // �⺻ ����� �˻�
	public DeliveryVO getDefaultDelivery(String memberId) {
		log.info("getDefaultDelivery()");
		DeliveryVO result = deliveryMapper.selectDefaultByMemberId(memberId);
		return result;
	} // end getDefaultDelivery

	// �⺻ ����� ī��Ʈ
	@Override
	public boolean hasDefaultAddress(String memberId) {
		log.info("hasDefaultAddress()");
		int count = deliveryMapper.cntIsDefault(memberId);
		log.info("�⺻ ����� ����: " + count);
		return  count > 0;
	}//end hasDefaultAddress()
	
	//����ϴ� deliveryId�� �⺻������� 1�� �ٲٱ�
	@Override
	public int updateNewDefault(int deliveryId, String memberId) {
		log.info("updateNewDefault() - deliveryId: " +  deliveryId);
		log.info("updateNewDefault() - memberId: " + memberId);
		int res = deliveryMapper.updateNewDefault(deliveryId, memberId);
		log.info(res + "�� �⺻����� ��� �Ϸ�");
		return res;
	}//end updateNewDefault

	
	//�ش��ϴ� memberid�� ������ �⺻����� ����� 0���� �ٲٱ�
	@Override
	public int updateDefault(String memberId) {
		log.info("updateDefault() - memberId : " + memberId);
		int res = deliveryMapper.updateDefault(memberId);
		log.info(res + "�� �⺻����� ���� �Ϸ�");
		return res;
	} //end updateNewDefault

	
	
	


	

	
}
