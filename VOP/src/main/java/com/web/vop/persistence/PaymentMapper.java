package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.PaymentVO;

@Mapper
public interface PaymentMapper {
	
	// paymentId 생성
	public int selectNextPaymentId();
	
	// 결제 결과 등록
	public int insertPayment(PaymentVO paymentVO);
	
	// 최근에 등록된 결제 결과 검색 by memberId
	public PaymentVO selectLastPayment(String memberId);
	
}
