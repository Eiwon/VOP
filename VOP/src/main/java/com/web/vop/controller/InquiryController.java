package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.vop.domain.InquiryVO;
import com.web.vop.service.InquiryService;


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
	public void listInquiryGET(Model model, Integer productId) {
		log.info("listInquiryGET()");
		log.info("productId : " + productId);
		List<InquiryVO> listInquiry = inquiryService.getAllInquiry(productId);
		
		log.info("listInquiry : " + listInquiry);
		
		model.addAttribute("listInquiry", listInquiry);
//		model.addAttribute("listInquiry", productName);
	}
	
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
