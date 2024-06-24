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

	// 입력한 권한을 가진 모든 유저 id 검색
	public List<String> selectIdByAuth(String memberAuth);
	
	// 이메일로 ID 찾기
	public List<String> selectIdByNameAndEmail(String memberEmail);
	
	// ID로 이메일 찾기
	public String selectEmailById(String memberId);
	
	// ID와 이메일로 ID 찾기
	public String selectIdByIdAndEmail(
			@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	// 판매자 권한 취소(권한을 일반으로 변경, 멤버십 등록된 유저라면 멤버십으로 변경
	public int revokeSellerAuth(String memberId);
	
}
