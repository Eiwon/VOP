package com.web.vop.persistence;

import java.util.List;

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
	
	// �����ȸ 
	public List<PaymentVO> selectPaymentByPaymentId(int paymentId);
}
