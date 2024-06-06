package com.web.vop.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.domain.MessageVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.persistence.Constant;
import com.web.vop.service.MemberService;
import com.web.vop.service.SellerService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/seller")
@Log4j
public class SellerController {

	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private MemberService memberService;
	
	
	@GetMapping("sellerRequest")
	public String sellerRequestGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("판매자 권한 신청 페이지로 이동");
		SellerVO sellerRequest = null;
		String memberId = memberDetails.getUsername();
		
		List<String> memberAuth = new ArrayList<>();
		Iterator<? extends GrantedAuthority> iterator = memberDetails.getAuthorities().iterator();
		
		while(iterator.hasNext()) {
			memberAuth.add(iterator.next().getAuthority());
		}
		
		if(memberAuth.contains("ROLE_일반")) {
			sellerRequest = sellerService.getMyRequest(memberId);
			if(sellerRequest == null) {
				return "seller/sellerRequest";
			}
			model.addAttribute("sellerRequest", sellerRequest);
		}
		return "seller/main";
	} // end sellerRequestGET
	
	@PostMapping("sellerRequest")
	public String sellerRequestPOST(SellerVO sellerVO, @AuthenticationPrincipal UserDetails memberDetails) {
		sellerVO.setMemberId(memberDetails.getUsername());
		log.info("sellerRequestPOST : " + sellerVO);
		sellerService.registerRequest(sellerVO);
		return "redirect:sellerRequest";
	} // end sellerRequestPOST
	
	@GetMapping("registerProduct")
	public String registerProductGET() {
		log.info("상품 등록 페이지로 이동");
		return "redirect:../product/register";
	} // end registerProductGET
	
	@GetMapping("/admin")
	public void adminGET() {
		log.info("관리자 페이지로 이동");
	} // end myInfoGet
	
	@GetMapping("/myProduct")
	public String listProductGET() {
		log.info("상품 조회 페이지 이동");
		return "redirect:../product/myProduct";
	} // end listProductGET
	

	// 자신의 판매자 권한 요청 조회
	@GetMapping("/mySellerReq")
	@ResponseBody
	public ResponseEntity<SellerVO> getMyRequest(@AuthenticationPrincipal MemberDetails memberDetails){
		log.info("내 권한요청 조회");
		String memberId = memberDetails.getUsername();
		SellerVO result = sellerService.getMyRequest(memberId);
			
		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
	} // end getMyRequest
		
		
	// 판매자 권한 요청 등록
	@PostMapping("/registerReq")
	@ResponseBody
	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 등록 : " + sellerVO);
		int res = sellerService.registerRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerRequest
		
	// 판매자 권한 요청 수정(유저)
	@PutMapping("/updateReq")
	@ResponseBody
	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 수정 : " + sellerVO);
		int res = sellerService.updateMemberContent(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end updateRequest

	// 판매자 권한 요청 승인/거절(관리자)
	@PutMapping("/approval")
	@ResponseBody
	public ResponseEntity<Integer> approveRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 승인 / 거절 : " + sellerVO.getMemberId());
		int res = sellerService.approveRequest(sellerVO);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
		
	// 판매자 권한 요청 삭제
	@DeleteMapping("/delete/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequest(@PathVariable("memberId") String memberId) {
		log.info("요청 삭제 : " + memberId);
		int res = sellerService.deleteRequest(memberId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequest

	
	// 판매자 권한 요청 조회
	@GetMapping("/wait")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getWaitRequest(Pagination pagination) {
		log.info("모든 승인 대기중인 권한요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState(Constant.STATE_APPROVAL_WAIT, pageMaker);
		log.info(list);
		
		pageMaker.update();
		Map<String, Object> resultMap = new HashMap<>(); // 반환할 타입이 2개이므로 pageMaker와 list를 담을 맵 생성
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getWaitRequest

	// 등록된 판매자 조회
	@GetMapping("/approved")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getApprovedRequest(Pagination pagination) {
		log.info("모든 승인된 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState(Constant.STATE_APPROVED, pageMaker);
		log.info(list);
		
		pageMaker.update();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pageMaker", pageMaker);
		resultMap.put("list", list);

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	} // end getAllRequest
	
	
	// 판매자 상세정보 팝업으로 이동
	@GetMapping("/popupSellerDetails")
	public void popupSellerDetailsGET(Model model, String memberId) {
		log.info("판매자 상세 정보 팝업 요청 " + memberId);
		SellerVO sellerVO = sellerService.getMyRequest(memberId);
		MemberVO memberVO = memberService.getMemberInfo(memberId);
		model.addAttribute("sellerVO", sellerVO);
		model.addAttribute("memberVO", memberVO);
	} // end popupSellerReqGET
	
	@GetMapping("/popupRegisterNotice")
	public void popupRegisterNoticeGET() {
		log.info("공지사항 등록 팝업 요청");
	} // end popupRegisterNotice
	
	@PostMapping("/notice")
	@ResponseBody
	public ResponseEntity<Integer> registerNotice(
			@RequestBody MessageVO messageVO, @AuthenticationPrincipal UserDetails memberDetails){
		log.info("공지사항 등록 요청 : " + messageVO);
		messageVO.setType("notice");
		messageVO.setWriterId(memberDetails.getUsername());
		int res = sellerService.registerNotice(messageVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end registerNotice
	
}
