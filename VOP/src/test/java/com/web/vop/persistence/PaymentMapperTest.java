package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.web.vop.config.RootConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.PaymentVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class PaymentMapperTest {

	@Autowired
	private PaymentMapper paymentMapper;
	 
	@Test
	public void test() {
		PaymentVO paymentVO = new PaymentVO();
        String memberId = "dnwpdud4";
        int paymentId = 2000;
        paymentVO.setChargeId("1234");
        paymentVO.setChargePrice(10000);
        paymentVO.setDeliveryAddress("test");
        paymentVO.setMemberId(memberId);
        paymentVO.setPaymentId(1500);
        paymentVO.setReceiverName(memberId);
        paymentVO.setReceiverPhone("01011111111");
        paymentVO.setRequirement("req");

        // selectNextPaymentIdTest();
        try {
            insertPaymentTest(paymentVO);
        } catch (DataIntegrityViolationException e) {
            log.error(e);
        }
        selectByMemberIdAndPaymentIdTest(memberId, paymentId);
        selectPaymentByPaymentIdTest(paymentId);
	}
	
	// paymentId 생성
    public void selectNextPaymentIdTest() {
        log.info(paymentMapper.selectNextPaymentId());
    } // end selectNextPaymentIdTest

    // 결제 결과 등록
    public void insertPaymentTest(PaymentVO paymentVO) throws DataIntegrityViolationException {
        log.info(paymentMapper.insertPayment(paymentVO));
    } // end insertPaymentTest

    // memberId와 paymentId로 결제 결과 검색
    public void selectByMemberIdAndPaymentIdTest(String memberId, int paymentId) {
        log.info(paymentMapper.selectByMemberIdAndPaymentId(memberId, paymentId));
    } // end selectByMemberIdAndPaymentIdTest

    // 배송조회
    public void selectPaymentByPaymentIdTest(int paymentId) {
        log.info(paymentMapper.selectPaymentByPaymentId(paymentId));
    } // end selectPaymentByPaymentIdTest
}
