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

}
