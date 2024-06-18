package com.web.vop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.domain.AnswerVO;
import com.web.vop.domain.InquiryVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.persistence.AnswerMapper;
import com.web.vop.service.InquiryService;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import com.web.vop.socket.AlarmHandler;


import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/inquiryRest")
@Log4j
public class InquiryRESTController {
	
	@Autowired
	private InquiryService inquiryService;
	
	@Autowired
	private WebSocketHandler alarmHandler;
	
	@PostMapping("/register") // POST : ���(����) �Է�
	public ResponseEntity<Integer> createInquiry(@RequestBody InquiryVO inquiryVO){
		log.info("createInquiry()");
		log.info("inquiryVO : " + inquiryVO);
		
		// inquiryVO �Է� �޾� ���(����) ���
		int result = inquiryService.createInquiry(inquiryVO);
		
		if(result == 1) {
			// ��� �˶� �۽�
	        ((AlarmHandler)alarmHandler).sendReplyAlarm(inquiryVO.getProductId());
		}
		
		// result���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createInquiry()
	
//	@GetMapping("/list/{productId}") // GET : ���(����) ����(all)  // ���߿� ������ �޴� �ſ� ���� �޶���
//	public ResponseEntity<List<InquiryVO>> readAllInquiry(
//			@PathVariable("productId") int productId){
//		log.info("readAllInquiry()");
//		
//		// productId�� �ش��ϴ� ���(����) list�� ��ü �˻�
//		List<InquiryVO> inquiryList = inquiryService.getAllInquiry(productId);
//		
//		
//		// list���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
//		return new ResponseEntity<List<InquiryVO>>(inquiryList, HttpStatus.OK);
//	}// end readAllInquiry()
	
	@GetMapping("/list/{productId}/{page}") // GET : ���(����) ����(all)  // ���߿� ������ �޴� �ſ� ���� �޶���
	public ResponseEntity<Map<String, Object>> readAllInquiry(
			Pagination pagination,
			@PathVariable("productId") int productId,
			@PathVariable("page") int page){
		log.info("readAllInquiry()");
		
		pagination.setPageNum(page);
		log.info("pagination : " + pagination);
		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);	
		log.info("�ʼ� �⺻�� : " + pageMaker.getPagination());
		
		// productId�� �ش��ϴ� ���(����) list�� ��ü �˻�
		List<InquiryVO> inquiryList = inquiryService.getAllInquiryPaging(productId, pageMaker);
		
		log.info("inquiryList : " + inquiryList);
		resultMap.put("inquiryList", inquiryList);
		resultMap.put("pageMaker", pageMaker);
		
		// list���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}// end readAllInquiry()
	

	@PutMapping("/modify") // PUT : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> updateInquiry(
			 @RequestBody InquiryVO inquiryVO
	         ){	
	      log.info("updateInquiry()");
	      
	      log.info("inquiryVO() : " + inquiryVO);
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = inquiryService.updateInquiry(inquiryVO.getProductId(), inquiryVO.getMemberId(), inquiryVO.getInquiryContent());
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateInquiry()
	
	@DeleteMapping("/delete") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> deleteInquiry(
			   @RequestBody InquiryVO inquiryVO) {
	      log.info("deleteInquiry()");
	      
	      log.info("productId : " + inquiryVO.getProductId());
	      log.info("memberId : " + inquiryVO.getMemberId());
	      
	      // productId�� �ش��ϴ� reviewId�� ���(����)
	      int result = inquiryService.deleteInquiry(inquiryVO.getProductId(), inquiryVO.getMemberId());
	
	      log.info(result + "�� ��� ����");
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end deleteInquiry()
	
	@GetMapping("/myList")
	public ResponseEntity<Map<String, Object>> myListInquiryGET(Pagination pagination,
			@AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("myListInquiry()");
		String memberId = memberDetails.getUsername();
		log.info("pagination : " + pagination);
		
		Map<String, Object> resultMap = new HashMap<>();
		
		//������ ����Ŀ�� �⺻ �ʼ��� ����
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		log.info("�ʼ� �⺻�� : " + pageMaker.getPagination());
		
		List<InquiryVO> listInquiry = inquiryService.getAllInquiryMemberIdPaging(memberId, pageMaker);
		
		log.info("listInquiry : " + listInquiry);
		log.info("pageMaker : " + pageMaker);
		
		resultMap.put("listInquiry", listInquiry);
		resultMap.put("pageMaker", pageMaker);
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}// myListInquiryGET 
}
