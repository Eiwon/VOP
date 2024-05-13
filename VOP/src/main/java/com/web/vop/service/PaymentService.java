package com.web.vop.service;

import com.web.vop.domain.PaymentVO;

public interface PaymentService {

	// paymentId 생성
	public int getNewPaymentId();
		
	// 결제 결과 등록
	public int registerPayment(PaymentVO paymentVO);
	
	// 최근에 등록된 결제 결과 검색 by memberId
	public PaymentVO getRecentPayment(String memberId);
		
}
