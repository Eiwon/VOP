package com.web.vop.service;

import java.util.Date;

public interface MembershipService {

		
	// ����� ��� 
	int registerMembership(String memberId);
		
	// ����� ���� ������Ʈ 
	void updateMemberAuth(String memberId);
		
	// ����� ��ȸ
	Date getExpiryDate(String memberId);
	
	// ����� ����
	int deleteMembership(String memberId);
	
	// ����� ���� �� ���� ������Ʈ
	void updateMemberAuthOnDelete(String memberId);
}
