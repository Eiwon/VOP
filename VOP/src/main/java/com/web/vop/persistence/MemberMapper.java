package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.MemberVO;

@Mapper
public interface MemberMapper {

	public int insertMember(MemberVO memberVo);

	public MemberVO selectByMemberId(String memberId);
	
	public String selectMemberIdById(String memberId);
	
	public String selectMemberWithPw(String memberId, String memberPw);
	
	public int updateMember(MemberVO memberVo);
	
	public int updateMemberAuth(String memberId, String memberAuth);
	
	public String selectByPhone(String memberPhone);
	
	public int updateMemberPw(String memberId, String memberPw);
	
	public int deleteMember(String memberId);
}
