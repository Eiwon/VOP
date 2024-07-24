package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataIntegrityViolationException;

import com.web.vop.domain.PaymentVO;

@Mapper
public interface PaymentMapper {
	
	// paymentId 생성
	public int selectNextPaymentId();
	
	public int updatePaymentSeq();
	
	// 결제 결과 등록
	public int insertPayment(PaymentVO paymentVO) throws DataIntegrityViolationException;
	
	// memberId와 paymentId로 결제 결과 검색
	public PaymentVO selectByMemberIdAndPaymentId(
			@Param("memberId") String memberId, @Param("paymentId") int paymentId);
	
	// 배송조회 
	public List<PaymentVO> selectPaymentByPaymentId(int paymentId);
}
