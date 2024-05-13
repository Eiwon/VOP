package com.web.vop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.PaymentVO;
import com.web.vop.persistence.PaymentMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class PaymentServiceImple implements PaymentService {
	
	@Autowired
	PaymentMapper paymentMapper;

	@Override
	public int getNewPaymentId() {
		log.info("getNewPaymentId()");
		return paymentMapper.selectNextPaymentId();
	} // end getNewPaymentId

	@Override
	public int registerPayment(PaymentVO paymentVO) {
		log.info("registerPayment()");
		return paymentMapper.insertPayment(paymentVO);
	} // end registerPayment

	@Override
	public PaymentVO getRecentPayment(String memberId) {
		log.info("getRecentPayment()");
		return paymentMapper.selectLastPayment(memberId);
	} // end getRecentPayment
	
	
	
}
