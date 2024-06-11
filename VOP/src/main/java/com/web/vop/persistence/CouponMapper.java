package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.util.Pagination;

@Mapper
public interface CouponMapper {

	// memberId로 쿠폰 조회
	//public List<CouponVO> selectByMemberId(String memberId);
	  
	// 지정 쿠폰 삭제
	//public int deleteCouponSelected(CouponVO couponVO);
	
	// 지정 수만큼 쿠폰 수 증감
	//public int updateCouponNum(CouponVO couponVO);
	
	// 지정 쿠폰 갯수 조회
	//public int selectCouponNum(CouponVO couponVO);
	
	// 신규 쿠폰 등록 
	public int insertCoupon(CouponVO couponVO);
	
	// 모든 쿠폰 조회 (페이징)
	public List<CouponVO> selectAllCoupon(Pagination pagination);
	
	// 모든 쿠폰 수 조회
	public int selectAllCouponCnt();
	  
	// 쿠폰 삭제
	public int deleteCouponById(int couponId);
}
