package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.MyCouponVO;

@Mapper
public interface CouponPocketMapper {
	
	// memberId�� ���� ��ȸ
	public List<MyCouponVO> selectByMemberId(String memberId);
	
	// ���� �߰�
	public int insertCouponPocket(CouponPocketVO couponPocketVO);
	
	// ���� ���� ���� �� ����
	public int updateCouponNum(
			@Param("couponId") int couponId, @Param("memberId") String memberId,
			@Param("couponNum") int couponNum);
	
	// ���� ���� ���� ��ȸ
	public int selectCouponNum(
			@Param("couponId") int couponId, @Param("memberId") String memberId);
	
	// ���� ���� ����
	public int deleteCouponById(
			@Param("couponId") int couponId, @Param("memberId") String memberId);
	
}
