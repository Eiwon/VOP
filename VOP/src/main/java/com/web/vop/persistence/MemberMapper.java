package com.web.vop.persistence;

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
}
