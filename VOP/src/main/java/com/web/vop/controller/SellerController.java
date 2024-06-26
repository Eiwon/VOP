package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketHandler;

import com.amazonaws.services.accessanalyzer.model.AccessDeniedException;
import com.web.vop.domain.AlertVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.PagingListDTO;
import com.web.vop.domain.SellerRequestDTO;
import com.web.vop.domain.SellerVO;
import com.web.vop.service.SellerService;
import com.web.vop.socket.AlarmHandler;
import com.web.vop.util.Constant;
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
	private WebSocketHandler alarmHandler;
	
	@GetMapping("/sellerRequest")
	public String sellerRequestGET(Model model, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("판매자 권한 신청 페이지로 이동");
		SellerVO sellerRequest = sellerService.getMyRequest(memberDetails.getUsername());
		String returnPath  = null;
		
		if(sellerRequest == null) {
			returnPath = "seller/sellerRequest";
		}else {
			model.addAttribute("sellerRequest", sellerRequest);
			returnPath = "seller/requestUpdate";
		}
		return returnPath;
	} // end sellerRequestGET
	
	@GetMapping("/main")
	public void sellerMainGET() {
		log.info("판매자 메인 페이지 이동");
	} // end sellerMainGET
	
	@PreAuthorize("#sellerVO.memberId == authentication.principal.username")
	@PostMapping("/sellerRequest")
	public String sellerRequestPOST(Model model, SellerVO sellerVO, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("sellerRequestPOST : " + sellerVO);
		AlertVO alertVO = new AlertVO();
		int res = sellerService.registerRequest(sellerVO);
		if(res == 1) {
			alertVO.setAlertMsg("신청이 등록되었습니다. 관리자 승인 후 상품 등록이 가능합니다.");
			alertVO.setRedirectUri("seller/sellerRequest");
		}else {
			alertVO.setAlertMsg("신청에 실패했습니다. 다시 시도해주세요.");
			alertVO.setRedirectUri("seller/sellerRequest");
		}
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end sellerRequestPOST
	
	@GetMapping("/registerProduct")
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
	

//	// 자신의 판매자 권한 요청 조회
//	@GetMapping("/mySellerReq")
//	@ResponseBody
//	public ResponseEntity<SellerVO> getMyRequest(@AuthenticationPrincipal MemberDetails memberDetails){
//		log.info("내 권한요청 조회");
//		String memberId = memberDetails.getUsername();
//		SellerVO result = sellerService.getMyRequest(memberId);
//			
//		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
//	} // end getMyRequest
		
		
//	// 판매자 권한 요청 등록
//	@PostMapping("/registerReq")
//	@ResponseBody
//	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO) {
//		log.info("요청 등록 : " + sellerVO);
//		int res = sellerService.registerRequest(sellerVO);
//
//		return new ResponseEntity<Integer>(res, HttpStatus.OK);
//	} // end registerRequest
		
//	// 판매자 권한 요청 수정(유저)
//	@PutMapping("/updateReq")
//	@ResponseBody
//	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO) {
//		log.info("요청 수정 : " + sellerVO);
//		int res = sellerService.updateMemberContent(sellerVO);
//
//		return new ResponseEntity<Integer>(res, HttpStatus.OK);
//	} // end updateRequest

	// 판매자 권한 요청 승인/거절(관리자)
	@PutMapping("/approval")
	@ResponseBody
	public ResponseEntity<Integer> decideRequest(@RequestBody SellerVO sellerVO) {
		log.info("요청 승인 / 거절 : " + sellerVO.getMemberId());
		int res = sellerService.approveRequest(sellerVO);
		String alarmMsg = null;
		
		if(res == 1) { // 결과 알람 송신
			alarmMsg = sellerVO.getRequestState().equals("approve") ? "판매자 등록 신청이 승인되었습니다." : "판매자 등록 신청이 거절되었습니다.";
			((AlarmHandler)alarmHandler).sendInstanceAlarm("판매자 등록 신청 결과", alarmMsg, sellerVO.getMemberId());
		}
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
		
	// 판매자 권한 취소
	@PutMapping("/revoke")
	@ResponseBody
	public ResponseEntity<Integer> revokeAuth(@RequestBody SellerVO sellerVO){
		log.info("판매자 권한 회수");
		int res = sellerService.revokeAuth(sellerVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end revokeAuth
	
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
	public ResponseEntity<PagingListDTO<SellerVO>> getWaitRequest(Pagination pagination) {
		log.info("모든 승인 대기중인 권한요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState(Constant.STATE_APPROVAL_WAIT, pageMaker);
		log.info(list);
		
		pageMaker.update();
		
		PagingListDTO<SellerVO> pagingList = new PagingListDTO<>();
		pagingList.setList(list);
		pagingList.setPageMaker(pageMaker);

		return new ResponseEntity<PagingListDTO<SellerVO>>(pagingList, HttpStatus.OK);
	} // end getWaitRequest

	// 등록된 판매자 조회
	@GetMapping("/approved")
	@ResponseBody
	public ResponseEntity<PagingListDTO<SellerVO>> getApprovedRequest(Pagination pagination) {
		log.info("모든 승인된 요청 조회");
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		List<SellerVO> list = sellerService.getRequestByState(Constant.STATE_APPROVED, pageMaker);
		log.info(list);
		
		pageMaker.update();
		
		PagingListDTO<SellerVO> pagingList = new PagingListDTO<>();
		pagingList.setList(list);
		pagingList.setPageMaker(pageMaker);

		return new ResponseEntity<PagingListDTO<SellerVO>>(pagingList, HttpStatus.OK);
	} // end getAllRequest
	
	
	// 판매자 상세정보 팝업으로 이동
	@GetMapping("/popupSellerDetails")
	public void popupSellerDetailsGET(Model model, String memberId) {
		log.info("판매자 상세 정보 팝업 요청 " + memberId);
		SellerRequestDTO sellerRequestDTO = sellerService.getSellerRequestDetails(memberId);
		model.addAttribute("sellerRequestDTO", sellerRequestDTO);
	} // end popupSellerReqGET
	
	@GetMapping("/popupRegisterNotice")
	public void popupRegisterNoticeGET() {
		log.info("공지사항 등록 팝업 요청");
	} // end popupRegisterNotice
	
	// 판매자 권한 재요청
	@PostMapping("/retry")
	public String retrySellerRequest(SellerVO sellerVO, @AuthenticationPrincipal UserDetails memberDetails) {
		sellerVO.setMemberId(memberDetails.getUsername());
		sellerService.retrySellerRequest(sellerVO);
		
		return "redirect:seller/sellerRequest";
	} // end retrySellerRequest
	
}
