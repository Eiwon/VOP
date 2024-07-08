package com.web.vop.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataIntegrityViolationException;


import com.web.vop.domain.MembershipExpiryDTO;
import com.web.vop.domain.MembershipVO;


@Mapper
public interface MembershipMapper {


	// MemberhshipId 생성
	int selectNextMembershipId();
	
	// 결제 등록
	//int insertMembershipPayment(MembershipVO membershipVO);
	
	// 멤버십 등록 
	int insertMembership(MembershipVO membershipVO) throws DataIntegrityViolationException;
	
	// 멤버십 권한 업데이트 
	int updateMemberAuthOnInsert(@Param("memberId") String memberId);
	
	// 멤버십 전체 조회
	MembershipVO selectByMemberId(@Param("memberId")String memberId);
	
	// memberId로 만료되지 않은 멤버십만 조회
	MembershipVO selectValidByMemberId(String memberId);
	
	// 멤버십 만료일 조회
	Date selectExpiryDate(@Param("memberId") String memberId);
	
	// 멤버십 삭제(해지)
	int deleteMembership(@Param("memberId") String memberId);

	// 멤버십 삭제 시 권한 업데이트
	int updateMemberAuthOnDelete(@Param("memberId") String memberId);
	
	// 멤버십 만료일 조회 (스케줄링)
	List<MembershipExpiryDTO> selectExpiryDateBySchedulling();
	
	// 멤버십 환불아이디(chargeId) 조회
	String selectChargeIdByMemberId(@Param("memberId") String memberId);
}
