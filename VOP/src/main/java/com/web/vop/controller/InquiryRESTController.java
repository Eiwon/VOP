package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.InquiryVO;
import com.web.vop.service.InquiryService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("���� ����1")
@Log4j
public class InquiryRESTController {
	
	@Autowired
	private InquiryService inquiryService;
	
	@PostMapping("/register") // POST : ���(����) �Է�
	public ResponseEntity<Integer> createInquiry(@RequestBody InquiryVO inquiryVO){
		log.info("createInquiry()");
		
		// inquiryVO �Է� �޾� ���(����) ���
		int result = inquiryService.createInquiry(inquiryVO);
		
		log.info(result + "�� ��� ���");
		// result���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createInquiry()
	
	
	@GetMapping("���� ����") // GET : ���(����) ����(all)  // ���߿� ������ �޴� �ſ� ���� �޶���
	public ResponseEntity<List<InquiryVO>> readAllInquiry(
			@PathVariable("productId") int productId){
		log.info("readAllInquiry()");
		
		// productId�� �ش��ϴ� ���(����) list�� ��ü �˻�
		List<InquiryVO> list = inquiryService.getAllInquiry(productId);
		
		// list���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<List<InquiryVO>>(list, HttpStatus.OK);
	}// end readAllInquiry()
	
	@PutMapping("/{productId}/{memberId}") // PUT : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> updateInquiry(
	         @PathVariable("productId") int reviewId,
	         @PathVariable("memberId") String memberId,
	         @RequestBody String  inquiryContent
	         ){	
	      log.info("updateInquiry()");
	      
	      // replyId Ȯ�� �α�
	      log.info("reviewId = " + reviewId);
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = inquiryService.updateInquiry(reviewId, memberId, inquiryContent);
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateInquiry()
	
	@DeleteMapping("/{productId}/{memberId}") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> deleteInquiry(
	         @PathVariable("productId") int productId,
	         @PathVariable("memberId") String memberId) {
	      log.info("deleteInquiry()");
	      
	      // productId�� �ش��ϴ� reviewId�� ���(����)
	      int result = inquiryService.deleteInquiry(productId, memberId);
	
	      log.info(result + "�� ��� ����");
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end deleteInquiry()
	
}
