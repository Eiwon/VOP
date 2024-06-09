package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.MyCouponVO;

@Mapper
public interface CouponPocketMapper {
	
	// memberId로 쿠폰 조회
	public List<MyCouponVO> selectByMemberId(String memberId);
	
	// 쿠폰 추가
	public int insertCouponPocket(CouponPocketVO couponPocketVO);
	
	// 지정 수로 쿠폰 수 변경
	public int updateCouponNum(
			@Param("couponId") int couponId, @Param("memberId") String memberId,
			@Param("couponNum") int couponNum);
	
	// 지정 쿠폰 갯수 조회
	public int selectCouponNum(
			@Param("couponId") int couponId, @Param("memberId") String memberId);
	
	// 지정 쿠폰 삭제
	public int deleteCouponById(
			@Param("couponId") int couponId, @Param("memberId") String memberId);
	
}
