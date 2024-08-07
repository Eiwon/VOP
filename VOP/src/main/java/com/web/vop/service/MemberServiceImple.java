package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.web.vop.domain.MemberVO;
import com.web.vop.persistence.MemberMapper;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.persistence.SellerMapper;
import com.web.vop.util.Constant;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MemberServiceImple implements MemberService{

	@Autowired
	MemberMapper memberMapper; 
	
	@Autowired
	SellerMapper sellerMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ProductMapper productMapper;
	
	@Override
	public int registerMember(MemberVO memberVO) {	// 회원 등록
		log.info("Member Service registerMember()");
		String password = memberVO.getMemberPw();
		password = passwordEncoder.encode(password);
		memberVO.setMemberPw(password);
		memberVO.setMemberAuth(Constant.AUTH_NORMAL);
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
	public boolean checkLogin(String memberId, String memberPw) {
		log.info("Member Service checkLogin()");
		MemberVO memberVO = memberMapper.selectByMemberId(memberId);
		boolean comp = passwordEncoder.matches(memberPw, memberVO.getMemberPw());
		log.info("비교 결과 : " + comp);
		
		return comp;
	} // end checkLogin

	@Override
	public int updateMember(MemberVO memberVO) { // 회원 정보 수정
		log.info("Member Service updateMember()");
		String memberPw = memberVO.getMemberPw();
		log.info("변경 요청 비밀번호 : " + memberPw);
		if(StringUtils.hasText(memberPw)) { // 비밀번호 변경 요청시, 비밀번호 암호화
			memberVO.setMemberPw(passwordEncoder.encode(memberPw));
		}else {
			memberVO.setMemberPw(null);
		}
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
		String encryptPw = passwordEncoder.encode(memberPw);
		int res = memberMapper.updateMemberPw(memberId, encryptPw);
		log.info(res + "행 수정 성공");
		return res;
	} // end updatePw

	@Transactional(value = "transactionManager")
	@Override
	public int deleteMember(String memberId) { // 회원 탈퇴
		log.info("Member Service deleteMember()");
		sellerMapper.deleteRequest(memberId);
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

	@Override
	public List<String> getAdminId() {
		log.info("getAdminId()");
		List<String> adminList = memberMapper.selectIdByAuth(Constant.AUTH_ADMIN);
		return adminList;
	} // end getAdminId

	@Override
	public List<String> getIdByEmail(String memberEmail) {
		log.info("getIdByNameAndEmail");
		List<String> memberIdList = memberMapper.selectIdByNameAndEmail(memberEmail);
		return memberIdList;
	} // end getIdByNameAndEmail
	
	@Override
	public String getEmailById(String memberId) {
		log.info("getEmailById");
		String memberEmail = memberMapper.selectEmailById(memberId);
		return memberEmail;
	} // end getEmailById
	
	@Override
	public String getIdByIdAndEmail(String memberId, String memberEmail) {
		log.info("getIdByIdAndEmail");
		String resultId = memberMapper.selectIdByIdAndEmail(memberId, memberEmail);
		return resultId;
	} // end getIdByIdAndEmail

	@Override
	public int isWithdrawable(String memberId) {
		log.info("isWithdrawable");
		// 등록한 상품이 있는지 확인
		int res = productMapper.selectByMemberIdCnt(memberId);
		
		return res;
	} // end isWithdrawable

	

}
