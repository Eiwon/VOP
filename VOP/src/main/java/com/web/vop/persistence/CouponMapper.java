package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponVO;

@Mapper
public interface CouponMapper {

	// memberId�� ���� ��ȸ
	public List<CouponVO> selectByMemberId(String memberId);
	  
	// ���� ���� ����
	public int deleteCouponSelected(CouponVO couponVO);
	
	// ���� ����ŭ ���� �� ����
	public int updateCouponNum(CouponVO couponVO);
	
	// ���� ���� ���� ��ȸ
	public int selectCouponNum(CouponVO couponVO);
}
