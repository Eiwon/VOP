package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponVO;
import com.web.vop.util.Pagination;

@Mapper
public interface CouponMapper {
	
	// 신규 쿠폰 등록 
	public int insertCoupon(CouponVO couponVO);
	
	// 모든 쿠폰 조회 (페이징)
	public List<CouponVO> selectAllCoupon(Pagination pagination);
	
	// 모든 쿠폰 수 조회
	public int selectAllCouponCnt();
	  
	// 쿠폰 삭제
	public int deleteCouponById(int couponId);
	
	// id로 쿠폰 검색
	public CouponVO selectById(int couponId);
	
	// 배포 중이지만 보유 or 사용하지 않은 쿠폰 검색
	public List<CouponVO> selectNotHadCoupon(String memberId);
	
	// 배포 여부 변경
	public int updatePublishingById(@Param("couponId") int couponId, @Param("publishing") int publishing);

	// 배포 중인 모든 쿠폰 검색
	public List<CouponVO> selectPublishingCoupon();


}
