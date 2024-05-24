package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.CouponVO;
import com.web.vop.persistence.CouponMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class CouponServiceImple implements CouponService{

	@Autowired
	CouponMapper couponMapper; 
	
	@Override
	public List<CouponVO> getByMemberId(String memberId) {
		log.info("getByMemberId()");
		return couponMapper.selectByMemberId(memberId);
	} // end getByMemberId

	@Override
	public int removeCoupon(CouponVO couponVO) {
		log.info("removeCoupon()");
		return couponMapper.deleteCouponSelected(couponVO);
	} // end removeCoupon

	@Override
	public int setCouponNum(CouponVO couponVO) {
		log.info("setCouponNum()");
		return couponMapper.updateCouponNum(couponVO);
	} // end setCouponNum

	@Override
	public int getCouponNum(CouponVO couponVO) {
		log.info("getCouponNum()");
		return couponMapper.selectCouponNum(couponVO);
	} // end getCouponNum

//	@Override
//	public int useUpCoupon(CouponVO couponVO) {
//		int couponNum = couponMapper.selectCouponNum(couponVO); // 현재 쿠폰 수 조회
//		int res = 0;
//		if(couponNum - 1 > 0) { // 사용 후, 쿠폰이 남아있다면 갯수 변경
//			couponVO.setCouponNum(couponNum -1);
//			res = couponMapper.updateCouponNum(couponVO);
//		}else { // 더 이상 남은 쿠폰이 없으면 삭제
//			res = couponMapper.deleteCouponSelected(couponVO);
//		}
//		
//		return res;
//	} // end useUpCoupon

	
}
