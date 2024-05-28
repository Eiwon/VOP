package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;

public interface PaymentService {

	// paymentId 생성
	public int getNewPaymentId();
		
	// 결제 정보 생성
	public PaymentWrapper makePaymentForm(int[] productIds, int[] productNums, String memberId);
	
	// 결제 결과 등록
	public int registerPayment(PaymentWrapper payment);
	
	// 최근에 등록된 결제 결과 검색 by memberId
	public PaymentWrapper getRecentPayment(String memberId);
	
	// 배송조회 
	public List<PaymentVO> getPaymentByPaymentId(int paymentId);
}
