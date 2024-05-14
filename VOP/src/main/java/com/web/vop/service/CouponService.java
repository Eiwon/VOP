package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.CouponVO;

public interface CouponService {

	// memberId�� ���� ��ȸ
	public List<CouponVO> getByMemberId(String memberId);
		  
	// ���� ���� ����
	public int removeCoupon(CouponVO couponVO);
		
	// ���� ���� ���� �� ����
	public int setCouponNum(CouponVO couponVO);
	
	// ���� ���� ���� ��ȸ
	public int getCouponNum(CouponVO couponVO); 
}
