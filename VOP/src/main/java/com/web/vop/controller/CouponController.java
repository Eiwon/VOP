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
		List<MyCouponVO> result = couponService.getMyUsableCouponPocket(memberId);
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
	
	@GetMapping("/getCoupon")
	public String getCouponGET(Model model, int couponId) {
		log.info("���� �߱� ������ ��û");
		CouponVO couponVO = couponService.getCouponById(couponId);
		String returnPath = null;
		
		if(couponVO == null) {
			log.info("�߸��� ���� ����");
			AlertVO alertVO = new AlertVO();
			alertVO.setAlertMsg("��ȿ���� ���� �����Դϴ�.");
			alertVO.setRedirectUri("close");
			model.addAttribute("alertVO", alertVO);
			returnPath = Constant.ALERT_PATH;
		}else {
			log.info("�˻��� ���� ���� : " + couponVO);
			model.addAttribute("couponVO", couponVO);
			returnPath = "coupon/getCoupon";
		}
		return returnPath;
	} // end getCouponGET
	
	@PostMapping("/getCoupon")
	public String getCouponInPocket(Model model, int couponId, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("���� �߱޹ޱ�");
		String memberId = memberDetails.getUsername();
		int res = couponService.addCouponPocket(couponId, memberId);
		
		AlertVO alertVO = new AlertVO();
		if(res == 1) {
			alertVO.setAlertMsg("������ �߱޵Ǿ����ϴ�.");
		}else if(res == 2) {
			alertVO.setAlertMsg("�̹� ������ �����Դϴ�.");
		}else {
			alertVO.setAlertMsg("�߱� ����! �ٽ� �õ����ּ���.");
		}
		alertVO.setRedirectUri("close");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end getCouponInPocket
	
	@GetMapping("/main")
	public void CouponMainGET() {
		log.info("���� ���� ������ �̵�");
	} // end CouponListGET
	
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<PagingListDTO<CouponVO>> getCouponList(Pagination pagination){
		log.info("���� ��ȸ");
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
		log.info("���� ��� ������ ��û");
	} // end registerGET
	
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<Integer> registerPOST(@RequestBody CouponVO couponVO) {
		log.info("���� ���");
		int res = couponService.registerCoupon(couponVO);
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerPOST
	
	@DeleteMapping("/delete")
	public ResponseEntity<Integer> deleteOriginalCoupon(@RequestBody List<Integer> couponIds){
		log.info("���� ����");
		int res = 0;
		for(int couponId : couponIds) {
			res += couponService.deleteCouponById(couponId);
		}
		log.info(res + "�� ���� ���� ����");
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteOriginalCoupon
	
}
