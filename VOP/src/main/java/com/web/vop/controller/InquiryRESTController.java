package com.web.vop.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.domain.InquiryDTO;
import com.web.vop.domain.InquiryVO;
import com.web.vop.domain.MemberDetails;

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
	
	@PreAuthorize("#inquiryVO.memberId == authentication.principal.username")
	@PostMapping("/register") // POST : ���(����) �Է�
	public ResponseEntity<Integer> createInquiry(@RequestBody InquiryVO inquiryVO){
		log.info("createInquiry()");
		log.info("inquiryVO : " + inquiryVO);
		
		// inquiryVO �Է� �޾� ���(����) ���
		int result = inquiryService.createInquiry(inquiryVO);
		
		if(result == 1) {
			// ��� �˶� �۽�
	        ((AlarmHandler)alarmHandler).sendInstanceAlarm(
	        		inquiryVO.getProductId(), 
	        		"���� �˸�", 
	        		"����� ��ǰ�� ���ǰ� ��ϵǾ����ϴ�", 
	        		"inquiry/list?productId=" + inquiryVO.getProductId()
	        		);
		}
		// result���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createInquiry()
	
	
	@GetMapping("/list/{productId}/{page}") // GET : ���(����) ����(all)  // ���߿� ������ �޴� �ſ� ���� �޶���
	public ResponseEntity<Map<String, Object>> readAllInquiry(
			Pagination pagination,
			@PathVariable("productId") int productId,
			@PathVariable("page") int page){
		log.info("readAllInquiry()");
		
		pagination.setPageNum(page);
		log.info("pagination : " + pagination);
		// ���ڿ� ���¿� ������Ʈ ���¸� ���� �ϱ� ���ؼ� ���
		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		// ���� �� �⺻ ������ ����
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
	
//	@GetMapping("/AllList/{page}") // GET : ���(����) ����(all)  // ���߿� ������ �޴� �ſ� ���� �޶���
//	public ResponseEntity<Map<String, Object>> readAllInquiry(
//			Pagination pagination,
//			@PathVariable("page") int page){
//		log.info("readAllInquiry()");
//		
//		pagination.setPageNum(page);
//		log.info("pagination : " + pagination);
//		// ���ڿ� ���¿� ������Ʈ ���¸� ���� �ϱ� ���ؼ� ���
//		Map<String, Object> resultMap = new HashMap<>();
//		PageMaker pageMaker = new PageMaker();
//		// ���� �� �⺻ ������ ����
//		pageMaker.setPagination(pagination);	
//		log.info("�ʼ� �⺻�� : " + pageMaker.getPagination());
//		
//		// productId�� �ش��ϴ� ���(����) list�� ��ü �˻�
//		List<InquiryVO> inquiryList = inquiryService.getAllInquiryPaging(productId, pageMaker);
//		
//		log.info("inquiryList : " + inquiryList);
//		resultMap.put("inquiryList", inquiryList);
//		resultMap.put("pageMaker", pageMaker);
//		
//		// list���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
//		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
//	}// end readAllInquiry()
	
	@PreAuthorize("#inquiryVO.memberId == authentication.principal.username")
	@PutMapping("/modify") // PUT : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> updateInquiry(
			 @RequestBody InquiryVO inquiryVO
	         ){	
	      log.info("updateInquiry()");
	      log.info("inquiryVO() : " + inquiryVO);
	      int productId = inquiryVO.getProductId();
	      String memberId = inquiryVO.getMemberId();
	      String inquiryContent = inquiryVO.getInquiryContent();
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = inquiryService.updateInquiry(productId, memberId, inquiryContent);
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateInquiry()
	
	@PreAuthorize("#inquiryVO.memberId == authentication.principal.username")
	@DeleteMapping("/delete") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> deleteInquiry(
			   @RequestBody InquiryVO inquiryVO) {
	      log.info("deleteInquiry()");
	      
	      int productId = inquiryVO.getProductId();
	      String memberId = inquiryVO.getMemberId();
	      
	      // productId�� �ش��ϴ� reviewId�� ���(����)
	      int result = inquiryService.deleteInquiry(productId, memberId);
	
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
		
//		List<InquiryVO> listInquiry = inquiryService.getAllInquiryMemberIdPaging(memberId, pageMaker);
		List<InquiryDTO> listInquiry = inquiryService.getAllMyInquiryDTO(memberId, pageMaker);
		
		log.info("listInquiry : " + listInquiry);
		log.info("pageMaker : " + pageMaker);
		
		resultMap.put("listInquiry", listInquiry);
		resultMap.put("pageMaker", pageMaker);
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}// myListInquiryGET 
}
