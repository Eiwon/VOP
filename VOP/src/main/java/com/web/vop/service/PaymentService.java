package com.web.vop.service;

import com.web.vop.domain.PaymentVO;

public interface PaymentService {

	// paymentId ����
	public int getNewPaymentId();
		
	// ���� ��� ���
	public int registerPayment(PaymentVO paymentVO);
	
	// �ֱٿ� ��ϵ� ���� ��� �˻� by memberId
	public PaymentVO getRecentPayment(String memberId);
		
}
