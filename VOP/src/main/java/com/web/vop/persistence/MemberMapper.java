package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.MemberVO;

@Mapper
public interface MemberMapper {

	public int insertMember(MemberVO memberVo);

	public MemberVO selectByMemberId(String memberId);
	
	public String selectMemberIdById(String memberId);
	
	public String selectMemberIdWithPw(@Param("memberId") String memberId, @Param("memberPw") String memberPw);
	
	public int updateMember(MemberVO memberVo);
	
	public int updateMemberAuth(@Param("memberId") String memberId, @Param("memberAuth") String memberAuth);
	
	public String selectAuthById(String memberId);
	
	public String selectByPhone(String memberPhone);
	
	public int updateMemberPw(@Param("memberId") String memberId, @Param("memberPw") String memberPw);
	
	public int deleteMember(String memberId);
	
	public String selectByNameAndPhone(@Param("memberName") String memberName, @Param("memberPhone") String memberPhone);

	// �Է��� ������ ���� ��� ���� id �˻�
	public List<String> selectIdByAuth(String memberAuth);
	
	// �̸��Ϸ� ID ã��
	public List<String> selectIdByNameAndEmail(String memberEmail);
	
	// ID�� �̸��� ã��
	public String selectEmailById(String memberId);
	
	// ID�� �̸��Ϸ� ID ã��
	public String selectIdByIdAndEmail(
			@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	// �Ǹ��� ���� ���(������ �Ϲ����� ����, ����� ��ϵ� ������� ��������� ����
	public int revokeSellerAuth(String memberId);
	
}
