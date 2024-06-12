package com.web.vop.persistence;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.MyCouponVO;

@Mapper
public interface CouponPocketMapper {
	
	// memberId로 쿠폰 조회
	public List<MyCouponVO> selectByMemberId(String memberId);
	
	// memberId로 사용 가능한 쿠폰 조회
	public List<MyCouponVO> selectUsableByMemberId(String memberId);
	
	// 쿠폰 추가
	public int insertCouponPocket(
			@Param("couponId") int couponId, @Param("memberId") String memberId) throws SQLIntegrityConstraintViolationException;
	
	// 쿠폰 활성화 / 비활성화
	public int updateIsUsed(
			@Param("couponId") int couponId, @Param("memberId") String memberId,
			@Param("isUsed") int isUsed);
	
	// 지정 쿠폰 삭제
	public int deleteCouponById(
			@Param("couponId") int couponId, @Param("memberId") String memberId);
	
	// 보유 중인 쿠폰인지 확인
	public Integer selectIdById(@Param("couponId") int couponId, @Param("memberId") String memberId);
}
