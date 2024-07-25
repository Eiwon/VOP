package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataIntegrityViolationException;

import com.web.vop.domain.PaymentVO;

@Mapper
public interface PaymentMapper {
	
	// paymentId ����
	public int selectNextPaymentId();
	
	public int updatePaymentSeq();
	
	// ���� ��� ���
	public int insertPayment(PaymentVO paymentVO) throws DataIntegrityViolationException;
	
	// memberId�� paymentId�� ���� ��� �˻�
	public PaymentVO selectByMemberIdAndPaymentId(
			@Param("memberId") String memberId, @Param("paymentId") int paymentId);
	
	// �����ȸ 
	public List<PaymentVO> selectPaymentByPaymentId(int paymentId);
}
