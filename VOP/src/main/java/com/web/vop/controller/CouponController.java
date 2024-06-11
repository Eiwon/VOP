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

	// ���� ������ 2����
	// 1. �� �������� �߱޵� ���� (memberId ����, ��� ����)
	// 2. �������� �߱��ϱ� ���� �����ڰ� ����� ���� (��� �Ұ�)
	
	@Autowired
	private CouponService couponService;
	
	@GetMapping("/myList")
	@ResponseBody
	public ResponseEntity<List<MyCouponVO>> getCouponList(@AuthenticationPrincipal UserDetails memberDetails){
		String memberId = memberDetails.getUsername();
		log.info("���� ����Ʈ ��û : " + memberId);
		List<MyCouponVO> result = couponService.getMyCouponPocket(memberId);
		log.info(result.size() + "�� ���� �˻�");
		return new ResponseEntity<List<MyCouponVO>>(result, HttpStatus.OK);
	} // end getCouponList
	
	@GetMapping("/myCoupon")
	public void myCouponGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("�� ������ ������ �̵� ��û");
		String memberId = memberDetails.getUsername();
		List<MyCouponVO> couponList = couponService.getMyCouponPocket(memberId);
		
		model.addAttribute("couponList", couponList);
	} // end myCouponGET
	
	@GetMapping("/list")
	public void getOriginalCoupon() {
		log.info("���� �߱޵ǰ� �ִ� ��� ���� �˻�");
		//List<CouponVO> list = couponService.getOriginalCoupon();
	} // end getOriginalCoupon
	
	@PostMapping("/register")
	public String registerCoupon(CouponVO couponVO) {
		log.info("���� ���");
		
		return "redirect:list";
	} // end registerCoupon
	
	@DeleteMapping("/{couponId}")
	public ResponseEntity<Integer> deleteOriginalCoupon(){
		log.info("���� ����");
		int res = 1;
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteOriginalCoupon
	
}
