package com.web.vop.persistence;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.MemberVO;
import com.web.vop.domain.MembershipVO;
import com.web.vop.domain.PaymentWrapper;

@Mapper
public interface MembershipMapper {


	// MemberhshipId ����
	int selectNextMembershipId();
	
	// ���� ���
	//int insertMembershipPayment(MembershipVO membershipVO);
	
	// ����� ��� 
	int insertMembership(MembershipVO membershipVO);
		
	// ����� ���� ������Ʈ 
	void updateMemberAuthOnInsert(@Param("memberId") String memberId);
	
	// ����� ��ü ��ȸ
	MembershipVO selectByMemberId(@Param("memberId")String memberId);
	
	// ����� ������ ��ȸ
	Date selectExpiryDate(@Param("memberId") String memberId);
	
	// ����� ����(����)
	int deleteMembership(@Param("memberId") String memberId);

	// ����� ���� �� ���� ������Ʈ
	void updateMemberAuthOnDelete(@Param("memberId") String memberId);
}
