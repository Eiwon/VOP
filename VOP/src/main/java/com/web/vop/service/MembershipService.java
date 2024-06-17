package com.web.vop.service;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.MembershipVO;
import com.web.vop.domain.PaymentWrapper;

public interface MembershipService {

	// MemberhshipId ����
	int getNextMembershipId();	
	
	// ���� ���� ���� 
	PaymentWrapper makeMembershipForm(String memberId);
	
	// ����� ��� 
	int registerMembership(PaymentWrapper payment);
		
	// ����� ���� ������Ʈ 
	void updateMemberAuth(String memberId);
	
	// ����� ��ü ��ȸ
	MembershipVO selectByMemberId(@Param("memberId")String memberId);
	
	// ����� ������ ��ȸ
	Date getExpiryDate(String memberId);
	
	// ����� ����
	int deleteMembership(String memberId);
	
	// ����� ���� �� ���� ������Ʈ
	void updateMemberAuthOnDelete(String memberId);
	
}
