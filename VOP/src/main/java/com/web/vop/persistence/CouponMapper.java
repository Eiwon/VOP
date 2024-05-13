package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponVO;

@Mapper
public interface CouponMapper {

	// memberId로 쿠폰 조회
	public List<CouponVO> selectByMemberId(String memberId);
	  
	// 지정 쿠폰 삭제
	public int deleteCouponSelected(CouponVO couponVO);
	
	// 지정 수만큼 쿠폰 수 증감
	public int updateCouponSelected(@Param("couponVO") CouponVO couponVO, @Param("increase") int increase);
	   
}
