package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.CouponVO;

public interface CouponService {

	// memberId로 쿠폰 조회
	public List<CouponVO> getByMemberId(String memberId);
		  
	// 지정 쿠폰 삭제
	public int removeCoupon(CouponVO couponVO);
		
	// 지정 수로 쿠폰 수 변경
	public int setCouponNum(CouponVO couponVO);
	
	// 지정 쿠폰 갯수 조회
	public int getCouponNum(CouponVO couponVO); 
}
