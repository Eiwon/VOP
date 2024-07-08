package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.web.vop.config.RootConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.DeliveryVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class DeliveryMapperTest {

	@Autowired
	private DeliveryMapper deliveryMapper;
	 
	@Test
	public void test() {
		DeliveryVO deliveryVo = new DeliveryVO();
        int deliveryId = 100;
        String memberId = "test1234";
        int paymentId = 1;
        deliveryVo.setDeliveryId(deliveryId);
        deliveryVo.setDeliveryAddressDetails("123");
        deliveryVo.setMemberId(memberId);
        deliveryVo.setReceiverAddress("aaa");
        deliveryVo.setReceiverName("test1111");
        deliveryVo.setReceiverPhone("01012341234");
        deliveryVo.setRequirement("요청사항");
		
		insertDeliveryTest(deliveryVo);
        updateDeliveryTest(deliveryVo);
        deleteDeliveryTest(deliveryId);
        selectByMemberIdTest(memberId);
        selectBydeliveryIdTest(deliveryId, memberId);
        cntIsDefaultTest(memberId);
        updateDefaultTest(memberId);
        updateNewDefaultTest(deliveryId, memberId);
        selectDefaultByMemberIdTest(memberId);
        selectDeliveryListTest(paymentId);
	}
	
	// 배송지 등록
    public void insertDeliveryTest(DeliveryVO deliveryVo) {
        log.info(deliveryMapper.insertDelivery(deliveryVo));
    } // end insertDeliveryTest

    // 배송지 수정 
    public void updateDeliveryTest(DeliveryVO deliveryVo) {
        log.info(deliveryMapper.updateDelivery(deliveryVo));
    } // end updateDeliveryTest

    // 배송지 삭제
    public void deleteDeliveryTest(int deliveryId) {
        log.info(deliveryMapper.deleteDelivery(deliveryId));
    } // end deleteDeliveryTest

    // 배송지 상세 조회 (주문조회)
    public void selectByMemberIdTest(String memberId) {
        log.info(deliveryMapper.selectByMemberId(memberId));
    } // end selectByMemberIdTest

    // 배송지 상세 조회 (수정페이지) by deliveryId and memberId
    public void selectBydeliveryIdTest(int deliveryId, String memberId) {
        log.info(deliveryMapper.selectBydeliveryId(deliveryId, memberId));
    } // end selectBydeliveryIdTest

    // 기본 배송지 개수 조회
    public void cntIsDefaultTest(String memberId) {
        log.info(deliveryMapper.cntIsDefault(memberId));
    } // end cntIsDefaultTest

    // memberid의 나머지 기본배송지 목록을 0으로 바꾸기
    public void updateDefaultTest(String memberId) {
        log.info(deliveryMapper.updateDefault(memberId));
    } // end updateDefaultTest

    // 등록하는 배송지의 isdefault = 1로 설정
    public void updateNewDefaultTest(int deliveryId, String memberId) {
        log.info(deliveryMapper.updateNewDefault(deliveryId, memberId));
    } // end updateNewDefaultTest

    // memberId로 기본 배송지 검색
    public void selectDefaultByMemberIdTest(String memberId) {
        log.info(deliveryMapper.selectDefaultByMemberId(memberId));
    } // end selectDefaultByMemberIdTest

    // 배송지 조회
    public void selectDeliveryListTest(int paymentId) {
        log.info(deliveryMapper.selectDeliveryList(paymentId));
    } // end selectDeliveryListTest
}
