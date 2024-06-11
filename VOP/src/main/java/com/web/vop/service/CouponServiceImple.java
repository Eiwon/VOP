package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.persistence.Constant;
import com.web.vop.persistence.CouponMapper;
import com.web.vop.persistence.CouponPocketMapper;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class CouponServiceImple implements CouponService{

	@Autowired
	CouponMapper couponMapper; 
	
	@Autowired
	CouponPocketMapper couponPocketMapper;

	@Override
	public int registerCoupon(CouponVO couponVO) {
		log.info("registerCoupon");
		int res = couponMapper.insertCoupon(couponVO);
		return res;
	} // end registerCoupon

	@Override
	public List<CouponVO> getAllCoupon(PageMaker pageMaker) {
		log.info("getAllCoupon");
		int totalCnt = couponMapper.selectAllCouponCnt();
		pageMaker.setTotalCount(totalCnt);
		List<CouponVO> list = couponMapper.selectAllCoupon(pageMaker.getPagination());
		
		return list;
	} // getAllCoupon
	
	@Override
	public int deleteCouponById(int couponId) {
		log.info("deleteCouponById");
		int res = couponMapper.deleteCouponById(couponId);
		return res;
	} // end deleteCouponById

	@Override
	public CouponVO getCouponById(int couponId) {
		log.info("getCouponById");
		CouponVO result = couponMapper.selectById(couponId);
		return result;
	} // end getCouponById
	
	@Override
	public List<MyCouponVO> getMyCouponPocket(String memberId) {
		log.info("getMyCouponPocket");
		List<MyCouponVO> list = couponPocketMapper.selectByMemberId(memberId);
		return list;
	} // end getMyCouponPocket

	@Override
	public List<MyCouponVO> getMyUsableCouponPocket(String memberId) {
		log.info("getMyUsableCouponPocket");
		List<MyCouponVO> list = couponPocketMapper.selectUsableByMemberId(memberId);
		return list;
	} // end getMyUsableCouponPocket
	
	@Override
	public int addCouponPocket(int couponId, String memberId) {
		log.info("addCouponPocket");
		int hadCouponId = couponPocketMapper.selectIdById(couponId, memberId);
		if(hadCouponId == 0) {
			return 2; // Áßº¹ ÄíÆù
		}else {
			return couponPocketMapper.insertCouponPocket(couponId, memberId);
		}
	} // end addCouponPocket


	@Override
	public int deleteCouponPocket(int couponId, String memberId) {
		log.info("deleteCouponPocket");
		int res = couponPocketMapper.deleteCouponById(couponId, memberId);
		return res;
	} // end deleteCouponPocket
	
	@Override
	public int useUpCoupon(int couponId, String memberId) {
		log.info("useUpCoupon");
		int res = couponPocketMapper.updateIsUsed(couponId, memberId, Constant.IS_USED);
		return res;
	} // end useUpCoupon




	
}
