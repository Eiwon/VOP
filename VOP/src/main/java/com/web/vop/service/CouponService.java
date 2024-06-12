package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

public interface CouponService {

	// 신규 쿠폰 등록
	public int registerCoupon(CouponVO couponVO);
		
	// 모든 쿠폰 조회 (페이징)
	public List<CouponVO> getAllCoupon(PageMaker pageMaker);
		  
	// 쿠폰 삭제
	public int deleteCouponById(int couponId);
	
	// id로 쿠폰 조회
	public CouponVO getCouponById(int couponId);
		
	// memberId로 쿠폰 조회
	public List<MyCouponVO> getMyCouponPocket(String memberId);
			
	// memberId로 사용 가능한 쿠폰 조회
	public List<MyCouponVO> getMyUsableCouponPocket(String memberId);
	
	// 쿠폰 추가
	public int addCouponPocket(int couponId, String memberId);
			
	// 지정 수로 쿠폰 수 변경
	//public int setCouponNum(CouponPocketVO couponPocketVO);
			
	// 지정 쿠폰 갯수 조회
	//public int getCouponNum(int couponId, String memberId);
			
	// 보유중인 지정 쿠폰 삭제
	public int deleteCouponPocket(int couponId, String memberId);
	
	// 쿠폰 사용 처리
	public int useUpCoupon(int couponId, String memberId);
	
	// 배포 중이지만 보유 or 사용하지 않은 쿠폰 검색
	public List<CouponVO> getNotHadCoupon(String memberId);
	
	// 배포 여부 변경
	public int setPublishing(int couponId, int publishing);
	
	// memberId로 쿠폰 조회
	//public List<CouponVO> getByMemberId(String memberId);
		  
	// 지정 쿠폰 삭제
	//public int removeCoupon(CouponVO couponVO);
		
	// 지정 수로 쿠폰 수 변경
	//public int setCouponNum(CouponVO couponVO);
	
	// 지정 쿠폰 갯수 조회
	//public int getCouponNum(CouponVO couponVO); 
	
	// 쿠폰 사용 처리
	//public int useUpCoupon(CouponVO couponVO);
	
}
