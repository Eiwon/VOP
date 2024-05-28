package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;

public interface PaymentService {

	// paymentId ����
	public int getNewPaymentId();
		
	// ���� ���� ����
	public PaymentWrapper makePaymentForm(int[] productIds, int[] productNums, String memberId);
	
	// ���� ��� ���
	public int registerPayment(PaymentWrapper payment);
	
	// �ֱٿ� ��ϵ� ���� ��� �˻� by memberId
	public PaymentWrapper getRecentPayment(String memberId);
	
	// �����ȸ 
	public List<PaymentVO> getPaymentByPaymentId(int paymentId);
}
