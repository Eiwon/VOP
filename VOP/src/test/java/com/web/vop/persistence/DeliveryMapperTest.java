package com.web.vop.persistence;

import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.DeliveryVO;


import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class})
@Log4j
public class DeliveryMapperTest {

	@Autowired
	private DeliveryMapper deliveryMapper;
	
	
	
	@Test
	public void test() {
		//insertDelivery();
		//getDeliveryById();
		//updateDelivery();
		//deleteDelivery();
		//getMemberId();
		//countIsDefault();
		//resetDefault();
		setNewDefault();

	}
	

	// ���ο� �⺻ ������� 1�� ����
	private void setNewDefault() {
		int res = deliveryMapper.updateNewDefault(1, "adminTest123");
		log.info("���ο� �⺻ ����� " + res + "���� ����");
		if(res == 0) {
			log.info("�⺻ ����� ���� ����");
		}
	}

	// ���� �⺻ ������� 0���� ������Ʈ
	private void resetDefault() {
		int res = deliveryMapper.updateDefault("adminTest123");
		log.info("�⺻ ����� : " + res + "�� ����");
		if(res == 0) {
			log.info("�ش� ���̵�� �⺻ ������� �����ϴ�.");
		}
	}

	// �⺻ ����� ��ȸ
	private void countIsDefault() {
		int res = deliveryMapper.cntIsDefault("adminTest123");
		log.info("cntIsDefault : " + res + "��");
		if (res == 0) {
            log.info("�⺻ ������� �����ϴ�.");
        }
	}

	// ����� ��ȸ by memberId
	private void getMemberId() {
		List<DeliveryVO> list = deliveryMapper.selectByMemberId("admin12345");
		log.info(list);
		
	}


	// ����� ��� test
	private void insertDelivery() {
		DeliveryVO delivery = new DeliveryVO(1, "admin12345", "admin", "���� ���α� ���̷� 31-6", "010-1111-3333", "����", "10����", 0);
		log.info("insertDelivery() : " + delivery);
		int res = deliveryMapper.insertDelivery(delivery);
		log.info(res + "�� ���");
	}

	// ����� ��ȸ 
	private void getDeliveryById() {
		DeliveryVO delivery = deliveryMapper.selectBydeliveryId(3, "admin12345");
		log.info("getDeliveryById() : " + delivery);
	}
	

	// ����� ���� test 
	private void updateDelivery() {
		DeliveryVO delivery = new DeliveryVO(3, "admin12345", "test", "���� ���빮�� 11����", "010-9999-8888", "����", "12-4", 1);
		log.info(delivery);
		int res = deliveryMapper.updateDelivery(delivery);
		log.info(res + "�� ����");
		 if (res == 0) {
	            log.warn("No rows updated. Please check if the record exists in the database.");
	        }
	}
	
	
	// ����� ���� test
	private void deleteDelivery() {
		DeliveryVO delivery = new DeliveryVO();
		int deliveryId = delivery.getDeliveryId();
		log.info(deliveryId);
		int res = deliveryMapper.deleteDelivery(4);
		log.info(res + "�� ����");
	}

	
	
	
	
	
	
	
}
