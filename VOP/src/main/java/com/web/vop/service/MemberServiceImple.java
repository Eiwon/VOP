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
	public int registerMember(MemberVO memberVO) {	// ȸ�� ���
		log.info("Member Service registerMember()");
		int res = memberMapper.insertMember(memberVO);
		log.info(res + "�� �߰� ����");
		return res;
	} // end registerMember

	@Override
	public MemberVO getMemberInfo(String memberId) {	// ���̵� ���� �� �˻�
		log.info("Member Service getMemberInfo()");
		MemberVO result = memberMapper.selectByMemberId(memberId);
		log.info("�˻��� ���� : " + result.toString());
		return result;
	} // end getMemberInfo
	
	@Override
	public String checkIdDup(String memberId) {	// ���̵� �ߺ� üũ(�������� �ʴ� ���̵�� null�� ����)
		log.info("Member Service checkIdDup()");
		String result = memberMapper.selectMemberIdById(memberId);
		log.info("�ߺ�üũ ��� : " + result);
		return result;
	} // end checkIdDup

	@Override
	public String checkLogin(String memberId, String memberPw) { // �α���
		log.info("Member Service checkLogin()");
		String result = memberMapper.selectMemberIdWithPw(memberId, memberPw);
		log.info("�α��� �õ� ��� : " + result);
		return result;
	} // end checkLogin

	@Override
	public int updateMember(MemberVO memberVO) { // ȸ�� ���� ����
		log.info("Member Service updateMember()");
		int res = memberMapper.updateMember(memberVO);
		log.info(res + "�� ���� ����");
		return res;
	} // end updateMember

	@Override
	public int updateAuth(String memberId, String memberAuth) { // ȸ�� ���� ����
		log.info("Member Service updateAuth()");
		int res = memberMapper.updateMemberAuth(memberId, memberAuth);
		log.info(res + "�� ���� ����");
		return res;
	} // end updateAuth
	
	@Override
	public String getIdByPhone(String memberPhone) { // ��ȭ��ȣ�� ID �˻�
		log.info("Member Service getIdByPhone() : " + memberPhone);
		String result = memberMapper.selectByPhone(memberPhone);
		log.info("��ȭ��ȣ�� �˻� ��� : " + result);
		return result;
	} // end getIdByPhone

	@Override
	public int updatePw(String memberId, String memberPw) { // ��й�ȣ ����
		log.info("Member Service updatePw()");
		int res = memberMapper.updateMemberPw(memberId, memberPw);
		log.info(res + "�� ���� ����");
		return res;
	} // end updatePw

	@Override
	public int deleteMember(String memberId) { // ȸ�� Ż��
		log.info("Member Service deleteMember()");
		int res = memberMapper.deleteMember(memberId);
		log.info(res + "�� ���� ����");
		return res;
	} // end deleteMember

	@Override
	public String findByNameAndPhone(String memberName, String memberPhone) {
		log.info("Member Service selectByNameAndPhone()");
		String result = memberMapper.selectByNameAndPhone(memberName, memberPhone);
		log.info("�˻� ��� : " + result);
		return result;
	} // end selectByNameAndPhone

	@Override
	public String getMemberAuth(String memberId) {
		log.info("getMemberAuth()");
		String auth = memberMapper.selectAuthById(memberId);
		return auth;
	} // end getMemberAuth

}
