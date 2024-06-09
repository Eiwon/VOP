package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MyCouponVO;
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
	public List<MyCouponVO> getMyCouponPocket(String memberId) {
		log.info("getMyCouponPocket");
		List<MyCouponVO> list = couponPocketMapper.selectByMemberId(memberId);
		return list;
	} // end getMyCouponPocket

	@Override
	public int addCouponPocket(CouponPocketVO couponPocketVO) {
		log.info("addCouponPocket");
		int res = couponPocketMapper.insertCouponPocket(couponPocketVO);
		return res;
	} // end addCouponPocket

//	@Override
//	public int setCouponNum(CouponPocketVO couponPocketVO) {
//		log.info("setCouponNum");
//		int res = couponPocketMapper.updateCouponNum(couponPocketVO);
//		return res;
//	} // end setCouponNum

//	@Override
//	public int getCouponNum(int couponId, String memberId) {
//		log.info("getCouponNum");
//		int couponNum = couponPocketMapper.selectCouponNum(couponId, memberId);
//		return couponNum;
//	} // end getCouponNum

	@Override
	public int deleteCouponPocket(int couponId, String memberId) {
		log.info("deleteCouponPocket");
		int res = couponPocketMapper.deleteCouponById(couponId, memberId);
		return res;
	} // end deleteCouponPocket
	
	@Override
	public int useUpCoupon(int couponId, String memberId) {
		int couponNum = couponPocketMapper.selectCouponNum(
				couponId, memberId); // 현재 쿠폰 수 조회
		int res = 0;
		
		if(couponNum - 1 > 0) { // 사용 후, 쿠폰이 남아있다면 갯수 변경
			res = couponPocketMapper.updateCouponNum(couponId, memberId, couponNum -1);
		}else { // 더 이상 남은 쿠폰이 없으면 삭제
			res = couponPocketMapper.deleteCouponById(couponId, memberId);
		}
		
		return res;
	} // end useUpCoupon
	
//	@Override
//	public List<CouponVO> getByMemberId(String memberId) {
//		log.info("getByMemberId()");
//		return couponMapper.selectByMemberId(memberId);
//	} // end getByMemberId
//
//	@Override
//	public int removeCoupon(CouponVO couponVO) {
//		log.info("removeCoupon()");
//		return couponMapper.deleteCouponSelected(couponVO);
//	} // end removeCoupon
//
//	@Override
//	public int setCouponNum(CouponVO couponVO) {
//		log.info("setCouponNum()");
//		return couponMapper.updateCouponNum(couponVO);
//	} // end setCouponNum
//
//	@Override
//	public int getCouponNum(CouponVO couponVO) {
//		log.info("getCouponNum()");
//		return couponMapper.selectCouponNum(couponVO);
//	} // end getCouponNum

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
