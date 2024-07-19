package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.InquiryVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.service.InquiryService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/inquiry")
@Log4j
public class InquiryController {

	@Autowired
	private InquiryService inquiryService;
	
	// ���� ��ü �˻� �ڵ�
	// �ش� ��ǰid�� �ִ� ���� �˻�
	// ���� ������� ��������ϴ�. �ٵ� ��ǰ �������� �񵿱�� �ҷ��Խ��ϴ�.
	@GetMapping("/list")
	public void listInquiryGET(Model model, Integer productId, Pagination pagination) {
		log.info("listInquiryGET()");
		log.info("productId : " + productId);
		
		//������ ����Ŀ�� �⺻ �ʼ��� ����
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		log.info("�ʼ� �⺻�� : " + pageMaker.getPagination());
		
		// ����¡ ó���� productId����ؼ� ����Ʈ �ҷ����� �ڵ�
		List<InquiryVO> listInquiry = inquiryService.getAllInquiryPaging(productId, pageMaker);
		
		log.info("listInquiry : " + listInquiry);
		
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("productId", productId);
		model.addAttribute("listInquiry", listInquiry);
	}
	
	@GetMapping("/myList")
	public void myListInquiryGET() {
		log.info("myListInquiryGET()");
	}
	
	@PreAuthorize("#memberDetails.username == authentication.principal.username")
	@GetMapping("/answerList")
	public void readAllAnswer(Model model, Integer productId, @AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("readAllAnswer()");
		String memberId = memberDetails.getUsername();
		log.info("memberId : " + memberId);
		
		// ȸ���� ���� �ۼ� �� ����Ʈ
		model.addAttribute("productId", productId);
		
		// ȸ���� �ۼ��� ����
		model.addAttribute("memberId", memberId);
	}
	
//	@GetMapping("/myList")
//	public void myListInquiryGET(Model model, String memberId, Pagination pagination) {
//		log.info("listInquiryGET()");
//		log.info("memberId : " + memberId);
//		
//		//������ ����Ŀ�� �⺻ �ʼ��� ����
//		PageMaker pageMaker = new PageMaker();
//		pageMaker.setPagination(pagination);
//		log.info("�ʼ� �⺻�� : " + pageMaker.getPagination());
//		
//		List<InquiryVO> listInquiry = inquiryService.getAllInquiryMemberIdPaging(memberId, pageMaker);
//		
//		log.info("listInquiry : " + listInquiry);
//		
//		model.addAttribute("pageMaker", pageMaker);
//		model.addAttribute("memberId", memberId);
//		model.addAttribute("listInquiry", listInquiry);
//	}
	
//	@PostMapping("/delete") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
//	public String deleteInquiry(Integer productId, String memberId){
//		log.info("deleteInquiry()");
//		
//		int result = inquiryService.deleteInquiry(productId, memberId);
//				
//		log.info(result + "�� ���� �Ϸ�");
//		return "redirect:../board/orderlist";
//	}   
//	
//	@DeleteMapping("/delete") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
//	   public ResponseEntity<Integer> deleteInquiry(
//			   @RequestBody InquiryVO inquiryVO) {
//	      log.info("deleteInquiry()");
//	      
//	      log.info("productId : " + inquiryVO.getProductId());
//	      log.info("memberId : " + inquiryVO.getMemberId());
//	      
//	      // productId�� �ش��ϴ� reviewId�� ���(����)
//	      int result = inquiryService.deleteInquiry(inquiryVO.getProductId(), inquiryVO.getMemberId());
//	
//	      log.info(result + "�� ���� ����");
//	      
//	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
//	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
//	   }// end deleteInquiry()
}
