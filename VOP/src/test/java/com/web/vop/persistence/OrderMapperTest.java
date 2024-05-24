package com.web.vop.persistence;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.PaymentVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class})
@Log4j
public class OrderMapperTest {

	@Autowired
	OrderMapper orderMapper;
	
	@Autowired 
	PaymentMapper paymentMapper;
	
	@Test
	public void test(){
		registerPayment();
		//registerOrder();
	}


	private void registerOrder() {
		OrderVO order = new OrderVO();
		order.setPaymentId(0);
		
	}


	// 예상 배송일 조회 
	private void getExpectDeliveryDate() {
		Date expectDate = orderMapper.selectByExpectDeliveryDate(0);
		log.info(expectDate);
	}
	
	// 임의로 paymentId 등록하기 
	private void registerPayment() {
		PaymentVO payment = new PaymentVO(0,"user01","경기 안성시 일죽면 화봉리 32","유저","01011112222","일반 : 문앞",10,10,10,100);
		int result = paymentMapper.insertPayment(payment);
		log.info(payment);
	}
	
	
}
