package com.web.vop.service;

import com.web.vop.domain.MemberVO;

public interface MemberService {

	// ȸ�� ���
	public int registerMember(MemberVO memberVO);
	
	// ���̵� ���� �� �˻�
	public MemberVO getMemberInfo(String memberId);
	
	// ���̵� �ߺ� üũ(�������� �ʴ� ���̵�� null�� ����)
	public String checkIdDup(String memberId);
	
	// �α���
	public String checkLogin(String memberId, String memberPw);
	
	// ȸ�� ���� ����
	public int updateMember(MemberVO memberVO);
	
	// ȸ�� ���� ����
	public int updateAuth(String memberId, String memberAuth);
	
	// ȸ�� ���� ��ȸ
	public String getMemberAuth(String memberId);
	
	// ��ȭ��ȣ�� ���̵� �˻�
	public String getIdByPhone(String memberPhone);
	
	// ��й�ȣ ����
	public int updatePw(String memberId, String memberPw);
	
	// ȸ�� Ż��
	public int deleteMember(String memberId);
	
	// �̸�, ��ȭ��ȣ�� ���̵� �˻�
	public String findByNameAndPhone(String memberName, String memberPhone);
	
}
