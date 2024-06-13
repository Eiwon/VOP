package com.web.vop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.MembershipVO;
import com.web.vop.persistence.MembershipMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MembershipServiceImple implements MembershipService{
	
	@Autowired
	MembershipMapper membershipMapper;

	
	@Override
	public int registerMembership(String memberId) { //멤버십 등록
		log.info("registerMembership : " + memberId);
		int res = membershipMapper.insertMembership(memberId);
		if(res == 1) {
			log.info("멤버십 등록 성공");
		}else {
			log.info("멤버십 등록 실패");
		}
		return res;
	}//end registerMembership() 

	
	@Override
	public void updateMemberAuth(String memberId) { //멤버십 권한 업데이트
		log.info("updateMemberAuth : " + memberId);
		membershipMapper.updateMemberAuthOnInsert(memberId);
		log.info("멤버십 권한 업데이트(멤버십)가 성공했습니다. " + memberId);
	}//end updateMemberAuth()

	
	@Override
	public MembershipVO selectByMemberId(String memberId) { // 멤버십 전체 조회 
		log.info("selectByMemberId : " + memberId);
		MembershipVO result = membershipMapper.selectByMemberId(memberId);
		log.info("멤버십 전체 조회 : " + result.toString());
		return result;
	}

	@Override
	public Date getExpiryDate(String memberId) { // 멤버십 만료일 조회
		log.info("getExpiryDate(). " + memberId);
		Date date = membershipMapper.selectExpiryDate(memberId);
		log.info("멤버십 만료 기간 : " + date);
		return date;
	}
	
	@Override
	public int deleteMembership(String memberId) { // 멤버십 삭제
		log.info("deleteMembership : " + memberId);
		int res = membershipMapper.deleteMembership(memberId);
		if(res == 1) {
			log.info("멤버십 삭제 성공");
		}else {
			log.info("멤버십 삭제 실패");
		}
		return res;
	}


	@Override
	public void updateMemberAuthOnDelete(String memberId) { // 멤버십 권한(일반유저) 변경
		log.info("updateMemberAuthOnDelete : " + memberId);
		membershipMapper.updateMemberAuthOnDelete(memberId);
		log.info("멤버십 권한 업데이트(일반유저)가 성공했습니다. " + memberId);
	}


	


	

	
	
	
}//end MembershipServiceImple()
