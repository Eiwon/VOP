package com.web.vop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.MemberVO;
import com.web.vop.persistence.MemberMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MemberServiceImple implements MemberService{

	@Autowired
	MemberMapper memberMapper; 
	
	@Override
	public int registerMember(MemberVO memberVO) {	// 회원 등록
		log.info("Member Service registerMember()");
		int res = memberMapper.insertMember(memberVO);
		log.info(res + "행 추가 성공");
		return res;
	} // end registerMember

	@Override
	public MemberVO getMemberInfo(String memberId) {	// 아이디 지정 상세 검색
		log.info("Member Service getMemberInfo()");
		MemberVO result = memberMapper.selectByMemberId(memberId);
		log.info("검색된 계정 : " + result.toString());
		return result;
	} // end getMemberInfo
	
	@Override
	public String checkIdDup(String memberId) {	// 아이디 중복 체크(존재하지 않는 아이디면 null을 리턴)
		log.info("Member Service checkIdDup()");
		String result = memberMapper.selectMemberIdById(memberId);
		log.info("중복체크 결과 : " + result);
		return result;
	} // end checkIdDup

	@Override
	public String checkLogin(String memberId, String memberPw) { // 로그인
		log.info("Member Service checkLogin()");
		String result = memberMapper.selectMemberIdWithPw(memberId, memberPw);
		log.info("로그인 시도 결과 : " + result);
		return result;
	} // end checkLogin

	@Override
	public int updateMember(MemberVO memberVO) { // 회원 정보 수정
		log.info("Member Service updateMember()");
		int res = memberMapper.updateMember(memberVO);
		log.info(res + "행 수정 성공");
		return res;
	} // end updateMember

	@Override
	public int updateAuth(String memberId, String memberAuth) { // 회원 권한 수정
		log.info("Member Service updateAuth()");
		int res = memberMapper.updateMemberAuth(memberId, memberAuth);
		log.info(res + "행 수정 성공");
		return res;
	} // end updateAuth
	
	@Override
	public String getIdByPhone(String memberPhone) { // 전화번호로 ID 검색
		log.info("Member Service getIdByPhone() : " + memberPhone);
		String result = memberMapper.selectByPhone(memberPhone);
		log.info("전화번호로 검색 결과 : " + result);
		return result;
	} // end getIdByPhone

	@Override
	public int updatePw(String memberId, String memberPw) { // 비밀번호 변경
		log.info("Member Service updatePw()");
		int res = memberMapper.updateMemberPw(memberId, memberPw);
		log.info(res + "행 수정 성공");
		return res;
	} // end updatePw

	@Override
	public int deleteMember(String memberId) { // 회원 탈퇴
		log.info("Member Service deleteMember()");
		int res = memberMapper.deleteMember(memberId);
		log.info(res + "행 삭제 성공");
		return res;
	} // end deleteMember

	@Override
	public String findByNameAndPhone(String memberName, String memberPhone) {
		log.info("Member Service selectByNameAndPhone()");
		String result = memberMapper.selectByNameAndPhone(memberName, memberPhone);
		log.info("검색 결과 : " + result);
		return result;
	} // end selectByNameAndPhone

	@Override
	public String getMemberAuth(String memberId) {
		log.info("getMemberAuth()");
		String auth = memberMapper.selectAuthById(memberId);
		return auth;
	} // end getMemberAuth

}
