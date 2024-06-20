package com.web.vop.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataIntegrityViolationException;


import com.web.vop.domain.MembershipExpiryDTO;
import com.web.vop.domain.MembershipVO;


@Mapper
public interface MembershipMapper {


	// MemberhshipId ����
	int selectNextMembershipId();
	
	// ���� ���
	//int insertMembershipPayment(MembershipVO membershipVO);
	
	// ����� ��� 
	int insertMembership(MembershipVO membershipVO) throws DataIntegrityViolationException;
	
	// ����� ���� ������Ʈ 
	void updateMemberAuthOnInsert(@Param("memberId") String memberId);
	
	// ����� ��ü ��ȸ
	MembershipVO selectByMemberId(@Param("memberId")String memberId);
	
	// ����� ������ ��ȸ
	Date selectExpiryDate(@Param("memberId") String memberId);
	
	// ����� ����(����)
	int deleteMembership(@Param("memberId") String memberId);

	// ����� ���� �� ���� ������Ʈ
	void updateMemberAuthOnDelete(@Param("memberId") String memberId);
	
	// ����� ������ ��ȸ (�����ٸ�)
	List<MembershipExpiryDTO> selectExpiryDateBySchedulling();
}
