package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.PaymentVO;

@Mapper
public interface PaymentMapper {
	
	// paymentId 생성
	public int selectNextPaymentId();
	
	// 결제 결과 등록
	public int insertPayment(PaymentVO paymentVO);
	
	// 최근에 등록된 결제 결과 검색 by memberId
	//public PaymentVO selectLastPayment(String memberId);
	
	// memberId와 paymentId로 결제 결과 검색
	public PaymentVO selectByMemberIdAndPaymentId(
			@Param("memberId") String memberId, @Param("paymentId") int paymentId);
	
	// 배송조회 
	public List<PaymentVO> selectPaymentByPaymentId(int paymentId);
}
