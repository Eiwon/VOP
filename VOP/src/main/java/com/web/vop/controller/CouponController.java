package com.web.vop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.service.CouponService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/coupon")
public class CouponController {

	// 쿠폰 종류는 2가지
	// 1. 각 유저에게 발급된 쿠폰 (memberId 존재, 사용 가능)
	// 2. 유저에게 발급하기 위해 관리자가 등록한 쿠폰 (사용 불가)
	
	@Autowired
	private CouponService couponService;
	
	@GetMapping("/myList")
	@ResponseBody
	public ResponseEntity<List<MyCouponVO>> getCouponList(@AuthenticationPrincipal UserDetails memberDetails){
		String memberId = memberDetails.getUsername();
		log.info("쿠폰 리스트 요청 : " + memberId);
		List<MyCouponVO> result = couponService.getMyCouponPocket(memberId);
		log.info(result.size() + "개 쿠폰 검색");
		return new ResponseEntity<List<MyCouponVO>>(result, HttpStatus.OK);
	} // end getCouponList
	
	@GetMapping("/myCoupon")
	public void myCouponGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("내 쿠폰함 페이지 이동 요청");
		String memberId = memberDetails.getUsername();
		List<MyCouponVO> couponList = couponService.getMyCouponPocket(memberId);
		
		model.addAttribute("couponList", couponList);
	} // end myCouponGET
	
	@GetMapping("/list")
	public void getOriginalCoupon() {
		log.info("현재 발급되고 있는 모든 쿠폰 검색");
		//List<CouponVO> list = couponService.getOriginalCoupon();
	} // end getOriginalCoupon
	
	@PostMapping("/register")
	public String registerCoupon(CouponVO couponVO) {
		log.info("쿠폰 등록");
		
		return "redirect:list";
	} // end registerCoupon
	
	@DeleteMapping("/{couponId}")
	public ResponseEntity<Integer> deleteOriginalCoupon(){
		log.info("쿠폰 삭제");
		int res = 1;
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteOriginalCoupon
	
}
