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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.AlertVO;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.domain.PagingListDTO;
import com.web.vop.persistence.Constant;
import com.web.vop.service.CouponService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

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
		List<MyCouponVO> result = couponService.getMyUsableCouponPocket(memberId);
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
	
	@GetMapping("/getCoupon")
	public String getCouponGET(Model model, int couponId) {
		log.info("쿠폰 발급 페이지 요청");
		CouponVO couponVO = couponService.getCouponById(couponId);
		String returnPath = null;
		
		if(couponVO == null) {
			log.info("잘못된 쿠폰 정보");
			AlertVO alertVO = new AlertVO();
			alertVO.setAlertMsg("유효하지 않은 쿠폰입니다.");
			alertVO.setRedirectUri("close");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
		}else {
			log.info("검색된 쿠폰 정보 : " + couponVO);
			model.addAttribute("couponVO", couponVO);
			returnPath = "coupon/getCoupon";
		}
		return returnPath;
	} // end getCouponGET
	
	@PostMapping("/getCoupon")
	public String getCouponInPocket(Model model, int couponId, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("쿠폰 발급받기");
		String memberId = memberDetails.getUsername();
		int res = couponService.addCouponPocket(couponId, memberId);
		
		AlertVO alertVO = new AlertVO();
		if(res == 1) {
			alertVO.setAlertMsg("쿠폰이 발급되었습니다.");
		}else if(res == 2) {
			alertVO.setAlertMsg("이미 수령한 쿠폰입니다.");
		}else {
			alertVO.setAlertMsg("발급 실패! 다시 시도해주세요.");
		}
		alertVO.setRedirectUri("close");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end getCouponInPocket
	
	@GetMapping("/main")
	public void CouponMainGET() {
		log.info("쿠폰 관리 페이지 이동");
	} // end CouponListGET
	
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<PagingListDTO<CouponVO>> getCouponList(Pagination pagination){
		log.info("쿠폰 조회");
		PageMaker pageMaker = new PageMaker();
		PagingListDTO<CouponVO> pagingListDTO = new PagingListDTO<>();

		pageMaker.setPagination(pagination);
		List<CouponVO> couponList = couponService.getAllCoupon(pageMaker);
		pageMaker.update();
		
		pagingListDTO.setPageMaker(pageMaker);
		pagingListDTO.setList(couponList);
		
		return new ResponseEntity<PagingListDTO<CouponVO>>(pagingListDTO, HttpStatus.OK);
	} // end getCouponList
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("쿠폰 등록 페이지 요청");
	} // end registerGET
	
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Integer> registerPOST(@RequestBody CouponVO couponVO) {
		log.info("쿠폰 등록");
		int res = couponService.registerCoupon(couponVO);
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerPOST
	
	@DeleteMapping("/delete")
	public ResponseEntity<Integer> deleteOriginalCoupon(@RequestBody List<Integer> couponIds){
		log.info("쿠폰 삭제");
		int res = 0;
		for(int couponId : couponIds) {
			res += couponService.deleteCouponById(couponId);
		}
		log.info(res + "개 쿠폰 삭제 성공");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteOriginalCoupon
	
}
