package com.web.vop.service;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.MembershipVO;

public interface MembershipService {

		
	// 멤버십 등록 
	int registerMembership(String memberId);
		
	// 멤버십 권한 업데이트 
	void updateMemberAuth(String memberId);
	
	// 멤버십 전체 조회
	MembershipVO selectByMemberId(@Param("memberId")String memberId);
	
	// 멤버십 만료일 조회
	Date getExpiryDate(String memberId);
	
	// 멤버십 삭제
	int deleteMembership(String memberId);
	
	// 멤버십 삭제 시 권한 업데이트
	void updateMemberAuthOnDelete(String memberId);
}
