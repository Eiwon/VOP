package com.web.vop.service;

import java.util.Date;

public interface MembershipService {

		
	// 멤버십 등록 
	int registerMembership(String memberId);
		
	// 멤버십 권한 업데이트 
	void updateMemberAuth(String memberId);
		
	// 멤버십 조회
	Date getExpiryDate(String memberId);
	
	// 멤버십 삭제
	int deleteMembership(String memberId);
	
	// 멤버십 삭제 시 권한 업데이트
	void updateMemberAuthOnDelete(String memberId);
}
