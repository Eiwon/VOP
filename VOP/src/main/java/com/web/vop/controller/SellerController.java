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
		log.info("�Ǹ��� ���� ��û �������� �̵�");
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
		log.info("�Ǹ��� ���� ������ �̵�");
	} // end sellerMainGET
	
	@PreAuthorize("#sellerVO.memberId == authentication.principal.username")
	@PostMapping("/sellerRequest")
	public String sellerRequestPOST(Model model, SellerVO sellerVO, @AuthenticationPrincipal UserDetails memberDetails) {
		log.info("sellerRequestPOST : " + sellerVO);
		AlertVO alertVO = new AlertVO();
		int res = sellerService.registerRequest(sellerVO);
		if(res == 1) {
			alertVO.setAlertMsg("��û�� ��ϵǾ����ϴ�. ������ ���� �� ��ǰ ����� �����մϴ�.");
			alertVO.setRedirectUri("seller/sellerRequest");
		}else {
			alertVO.setAlertMsg("��û�� �����߽��ϴ�. �ٽ� �õ����ּ���.");
			alertVO.setRedirectUri("seller/sellerRequest");
		}
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end sellerRequestPOST
	
	@GetMapping("/registerProduct")
	public String registerProductGET() {
		log.info("��ǰ ��� �������� �̵�");
		return "redirect:../product/register";
	} // end registerProductGET
	
	@GetMapping("/admin")
	public void adminGET() {
		log.info("������ �������� �̵�");
	} // end myInfoGet
	
	@GetMapping("/myProduct")
	public String listProductGET() {
		log.info("��ǰ ��ȸ ������ �̵�");
		return "redirect:../product/myProduct";
	} // end listProductGET
	

//	// �ڽ��� �Ǹ��� ���� ��û ��ȸ
//	@GetMapping("/mySellerReq")
//	@ResponseBody
//	public ResponseEntity<SellerVO> getMyRequest(@AuthenticationPrincipal MemberDetails memberDetails){
//		log.info("�� ���ѿ�û ��ȸ");
//		String memberId = memberDetails.getUsername();
//		SellerVO result = sellerService.getMyRequest(memberId);
//			
//		return new ResponseEntity<SellerVO>(result, HttpStatus.OK);
//	} // end getMyRequest
		
		
//	// �Ǹ��� ���� ��û ���
//	@PostMapping("/registerReq")
//	@ResponseBody
//	public ResponseEntity<Integer> registerRequest(@RequestBody SellerVO sellerVO) {
//		log.info("��û ��� : " + sellerVO);
//		int res = sellerService.registerRequest(sellerVO);
//
//		return new ResponseEntity<Integer>(res, HttpStatus.OK);
//	} // end registerRequest
		
//	// �Ǹ��� ���� ��û ����(����)
//	@PutMapping("/updateReq")
//	@ResponseBody
//	public ResponseEntity<Integer> updateRequest(@RequestBody SellerVO sellerVO) {
//		log.info("��û ���� : " + sellerVO);
//		int res = sellerService.updateMemberContent(sellerVO);
//
//		return new ResponseEntity<Integer>(res, HttpStatus.OK);
//	} // end updateRequest

	// �Ǹ��� ���� ��û ����/����(������)
	@PutMapping("/approval")
	@ResponseBody
	public ResponseEntity<Integer> decideRequest(@RequestBody SellerVO sellerVO) {
		log.info("��û ���� / ���� : " + sellerVO.getMemberId());
		int res = sellerService.approveRequest(sellerVO);
		String alarmMsg = null;
		
		if(res == 1) { // ��� �˶� �۽�
			alarmMsg = sellerVO.getRequestState().equals("approve") ? "�Ǹ��� ��� ��û�� ���εǾ����ϴ�." : "�Ǹ��� ��� ��û�� �����Ǿ����ϴ�.";
			((AlarmHandler)alarmHandler).sendInstanceAlarm("�Ǹ��� ��� ��û ���", alarmMsg, sellerVO.getMemberId());
		}
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end refuseRequest
		
	// �Ǹ��� ���� ���
	@PutMapping("/revoke")
	@ResponseBody
	public ResponseEntity<Integer> revokeAuth(@RequestBody SellerVO sellerVO){
		log.info("�Ǹ��� ���� ȸ��");
		int res = sellerService.revokeAuth(sellerVO);
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end revokeAuth
	
	// �Ǹ��� ���� ��û ����
	@DeleteMapping("/delete/{memberId}")
	@ResponseBody
	public ResponseEntity<Integer> deleteRequest(@PathVariable("memberId") String memberId) {
		log.info("��û ���� : " + memberId);
		int res = sellerService.deleteRequest(memberId);

		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end deleteRequest

	
	// �Ǹ��� ���� ��û ��ȸ
	@GetMapping("/wait")
	@ResponseBody
	public ResponseEntity<PagingListDTO<SellerVO>> getWaitRequest(Pagination pagination) {
		log.info("��� ���� ������� ���ѿ�û ��ȸ");
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

	// ��ϵ� �Ǹ��� ��ȸ
	@GetMapping("/approved")
	@ResponseBody
	public ResponseEntity<PagingListDTO<SellerVO>> getApprovedRequest(Pagination pagination) {
		log.info("��� ���ε� ��û ��ȸ");
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
	
	
	// �Ǹ��� ������ �˾����� �̵�
	@GetMapping("/popupSellerDetails")
	public void popupSellerDetailsGET(Model model, String memberId) {
		log.info("�Ǹ��� �� ���� �˾� ��û " + memberId);
		SellerRequestDTO sellerRequestDTO = sellerService.getSellerRequestDetails(memberId);
		model.addAttribute("sellerRequestDTO", sellerRequestDTO);
	} // end popupSellerReqGET
	
	@GetMapping("/popupRegisterNotice")
	public void popupRegisterNoticeGET() {
		log.info("�������� ��� �˾� ��û");
	} // end popupRegisterNotice
	
	// �Ǹ��� ���� ���û
	@PostMapping("/retry")
	public String retrySellerRequest(SellerVO sellerVO, @AuthenticationPrincipal UserDetails memberDetails) {
		sellerVO.setMemberId(memberDetails.getUsername());
		sellerService.retrySellerRequest(sellerVO);
		
		return "redirect:seller/sellerRequest";
	} // end retrySellerRequest
	
}
