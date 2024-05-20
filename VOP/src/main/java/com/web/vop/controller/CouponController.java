package com.web.vop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.CouponVO;
import com.web.vop.service.CouponService;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/coupon")
public class CouponController {

	@Autowired
	private CouponService couponService;
	
	@GetMapping("/myList")
	@ResponseBody
	public ResponseEntity<List<CouponVO>> getCouponList(HttpServletRequest request){
		String memberId = (String)request.getSession().getAttribute("memberId");
		log.info("쿠폰 리스트 요청 : " + memberId);
		
		List<CouponVO> result = couponService.getByMemberId(memberId);
		log.info(result.size() + "개 쿠폰 검색");
		return new ResponseEntity<List<CouponVO>>(result, HttpStatus.OK);
	} // end getCouponList
}
