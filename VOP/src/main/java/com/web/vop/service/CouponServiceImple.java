package com.web.vop.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.persistence.CouponMapperTest;
import com.web.vop.persistence.CouponPocketMapper;
import com.web.vop.util.Constant;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class CouponServiceImple implements CouponService{

	@Autowired
	CouponMapperTest couponMapper; 
	
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
		int res = 0;
		try {
			res = couponPocketMapper.insertCouponPocket(couponId, memberId);
		} catch (DataIntegrityViolationException e) {
			log.info("이미 수령한 쿠폰");
			res = 2;
		}
		return res;
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

	@Override
	public List<CouponVO> getNotHadCoupon(String memberId) {
		log.info("getNotHadCoupon");
		List<CouponVO> list = couponMapper.selectNotHadCoupon(memberId);
		return list;
	} // end getNotHadCoupon

	@Transactional(value = "transactionManager")
	@Override
	public int setPublishing(List<Integer> couponIdList, int publishing) {
		log.info("setPublishing");
		int res = 0;
		for(int couponId : couponIdList) {
			res += couponMapper.updatePublishingById(couponId, publishing);
		}
		return res;
	} // end setPublishing

	@Override
	public List<CouponVO> getPublishingCoupon() {
		log.info("getPublishingCoupon");
		List<CouponVO> list = couponMapper.selectPublishingCoupon();
		return list;
	} // end getPublishingCoupon




	
}
