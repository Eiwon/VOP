package com.web.vop.persistence;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.MemberVO;
import com.web.vop.domain.MembershipVO;

@Mapper
public interface MembershipMapper {


	// 멤버십 등록 
	int insertMembership(@Param("memberId") String memberId);
		
	// 멤버십 권한 업데이트 
	void updateMemberAuthOnInsert(@Param("memberId") String memberId);
	
	// 멤버십 전체 조회
	MembershipVO selectByMemberId(@Param("memberId")String memberId);
	
	// 멤버십 만료일 조회
	Date selectExpiryDate(@Param("memberId") String memberId);
	
	// 멤버십 삭제(해지)
	int deleteMembership(@Param("memberId") String memberId);

	// 멤버십 삭제 시 권한 업데이트
	void updateMemberAuthOnDelete(@Param("memberId") String memberId);
}
