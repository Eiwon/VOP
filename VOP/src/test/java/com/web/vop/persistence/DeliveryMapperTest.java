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
	

	// 새로운 기본 배송지를 1로 설정
	private void setNewDefault() {
		int res = deliveryMapper.updateNewDefault(1, "adminTest123");
		log.info("새로운 기본 배송지 " + res + "수정 성공");
		if(res == 0) {
			log.info("기본 배송지 수정 실패");
		}
	}

	// 기존 기본 배송지를 0으로 업데이트
	private void resetDefault() {
		int res = deliveryMapper.updateDefault("adminTest123");
		log.info("기본 배송지 : " + res + "개 수정");
		if(res == 0) {
			log.info("해당 아이디는 기본 배송지가 없습니다.");
		}
	}

	// 기본 배송지 조회
	private void countIsDefault() {
		int res = deliveryMapper.cntIsDefault("adminTest123");
		log.info("cntIsDefault : " + res + "개");
		if (res == 0) {
            log.info("기본 배송지가 없습니다.");
        }
	}

	// 배송지 조회 by memberId
	private void getMemberId() {
		List<DeliveryVO> list = deliveryMapper.selectByMemberId("admin12345");
		log.info(list);
		
	}


	// 배송지 등록 test
	private void insertDelivery() {
		DeliveryVO delivery = new DeliveryVO(1, "admin12345", "admin", "서울 종로구 북촌로 31-6", "010-1111-3333", "문앞", "10번지", 0);
		log.info("insertDelivery() : " + delivery);
		int res = deliveryMapper.insertDelivery(delivery);
		log.info(res + "행 등록");
	}

	// 배송지 조회 
	private void getDeliveryById() {
		DeliveryVO delivery = deliveryMapper.selectBydeliveryId(3, "admin12345");
		log.info("getDeliveryById() : " + delivery);
	}
	

	// 배송지 수정 test 
	private void updateDelivery() {
		DeliveryVO delivery = new DeliveryVO(3, "admin12345", "test", "서울 동대문구 11번지", "010-9999-8888", "현관", "12-4", 1);
		log.info(delivery);
		int res = deliveryMapper.updateDelivery(delivery);
		log.info(res + "행 수정");
		 if (res == 0) {
	            log.warn("No rows updated. Please check if the record exists in the database.");
	        }
	}
	
	
	// 배송지 삭제 test
	private void deleteDelivery() {
		DeliveryVO delivery = new DeliveryVO();
		int deliveryId = delivery.getDeliveryId();
		log.info(deliveryId);
		int res = deliveryMapper.deleteDelivery(4);
		log.info(res + "행 삭제");
	}

	
	
	
	
	
	
	
}
