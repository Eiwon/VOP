package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.AnswerVO;

import com.web.vop.service.AnswerService;


import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/answer")
@Log4j
public class AnswerRESTController {
	
	@Autowired
	private AnswerService answerService;
	
	// ����(�亯) ���	
	@PostMapping("/register") // POST : ����(�亯) �Է�
	public ResponseEntity<Integer> createAnswer(@RequestBody AnswerVO answerVO){
		log.info("createAnswer()");
		
		// inquiryVO �Է� �޾� ����(�亯) ���
		int result = answerService.createAnswer(answerVO);
		
		log.info(result + "�� ����(�亯) ���");
		// result���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createAnswer()
	
	 //����(�亯) ��ü �˻�
	@GetMapping("/list/{productId}") // GET : ����(�亯) ����(all)  // ���߿� ������ �޴� �ſ� ���� �޶���
	public ResponseEntity<List<AnswerVO>> readAllAnswer(
			@PathVariable("productId") int productId){
		log.info("readAllAnswer()");
		
		// productId�� �ش��ϴ� ����(�亯) list�� ��ü �˻�
		List<AnswerVO> list = answerService.getAllAnswer(productId);
		
		// list���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<List<AnswerVO>>(list, HttpStatus.OK);
	}// end readAllAnswer()
	
	// ����(�亯) ����
	@PutMapping("/modify") // PUT : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> updateAnswer(
			   @RequestBody AnswerVO answerVO
	         ){	
	      log.info("updateAnswer()");
	      
	      int inquiryId = answerVO.getInquiryId();
	      String memberId = answerVO.getMemberId();
	      String answerContent = answerVO.getAnswerContent();
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = answerService.updateAnswer(inquiryId, memberId, answerContent);
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateAnswer()
	
	// ����(�亯) ����
	@DeleteMapping("/delete") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> deleteAnswer(
			   @RequestBody AnswerVO answerVO) {
	      log.info("deleteAnswer()");
	      
	      int inquiryId = answerVO.getInquiryId();
	      
	      String memberId = answerVO.getMemberId();
	      
	      // productId�� �ش��ϴ� reviewId�� ���(����)
	      int result = answerService.deleteAnswer(inquiryId, memberId);
	
	      log.info(result + "�� ��� ����");
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end deleteInquiry()

}
