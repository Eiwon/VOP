package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.PaymentVO;

@Mapper
public interface PaymentMapper {
	
	// paymentId ����
	public int selectNextPaymentId();
	
	// ���� ��� ���
	public int insertPayment(PaymentVO paymentVO);
	
	// �ֱٿ� ��ϵ� ���� ��� �˻� by memberId
	public PaymentVO selectLastPayment(String memberId);
	
}
