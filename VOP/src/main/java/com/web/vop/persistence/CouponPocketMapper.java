package com.web.vop.persistence;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.MyCouponVO;

@Mapper
public interface CouponPocketMapper {
	
	// memberId�� ���� ��ȸ
	public List<MyCouponVO> selectByMemberId(String memberId);
	
	// memberId�� ��� ������ ���� ��ȸ
	public List<MyCouponVO> selectUsableByMemberId(String memberId);
	
	// ���� �߰�
	public int insertCouponPocket(
			@Param("couponId") int couponId, @Param("memberId") String memberId) throws SQLIntegrityConstraintViolationException;
	
	// ���� Ȱ��ȭ / ��Ȱ��ȭ
	public int updateIsUsed(
			@Param("couponId") int couponId, @Param("memberId") String memberId,
			@Param("isUsed") int isUsed);
	
	// ���� ���� ����
	public int deleteCouponById(
			@Param("couponId") int couponId, @Param("memberId") String memberId);
	
	// ���� ���� �������� Ȯ��
	public Integer selectIdById(@Param("couponId") int couponId, @Param("memberId") String memberId);
}
