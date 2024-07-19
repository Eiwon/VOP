package com.web.vop.service;

import java.util.List;


import com.web.vop.domain.MemberVO;

public interface MemberService {

	// 회원 등록
	public int registerMember(MemberVO memberVO);
	
	// 아이디 지정 상세 검색
	public MemberVO getMemberInfo(String memberId);
	
	// 아이디 중복 체크(존재하지 않는 아이디면 null을 리턴)
	public String checkIdDup(String memberId);
	
	// 로그인
	public boolean checkLogin(String memberId, String memberPw);
	
	// 회원 정보 수정
	public int updateMember(MemberVO memberVO);
	
	// 회원 권한 변경
	public int updateAuth(String memberId, String memberAuth);
	
	// 회원 권한 조회
	public String getMemberAuth(String memberId);
	
	// 전화번호로 아이디 검색
	public String getIdByPhone(String memberPhone);
	
	// 비밀번호 변경
	public int updatePw(String memberId, String memberPw);
	
	// 회원 탈퇴
	public int deleteMember(String memberId);
	
	// 이름, 전화번호로 아이디 검색
	public String findByNameAndPhone(String memberName, String memberPhone);
	
	// 권한이 관리자인 모든 유저 id 검색
	public List<String> getAdminId();
	
	// 이름과 이메일로 ID 찾기
	public List<String> getIdByEmail(String memberEmail);
	
	// ID로 이메일 찾기
	public String getEmailById(String memberId);	
		
	// ID와 이메일로 ID 찾기
	public String getIdByIdAndEmail(String memberId, String memberEmail);
	
	// 탈퇴 가능한 유저인지 검사
	public int isWithdrawable(String memberId);
	
}
