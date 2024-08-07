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
	@PostMapping("/register") // POST : 댓글(문의) 입력
	public ResponseEntity<Integer> createInquiry(@RequestBody InquiryVO inquiryVO){
		log.info("createInquiry()");
		log.info("inquiryVO : " + inquiryVO);
		
		// inquiryVO 입력 받아 댓글(문의) 등록
		int result = inquiryService.createInquiry(inquiryVO);
		
		if(result == 1) {
			// 댓글 알람 송신
	        ((AlarmHandler)alarmHandler).sendInstanceAlarm(
	        		inquiryVO.getProductId(), 
	        		"문의 알림", 
	        		"등록한 상품에 문의가 등록되었습니다", 
	        		"inquiry/list?productId=" + inquiryVO.getProductId()
	        		);
		}
		// result값을 전송하여 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createInquiry()
	
	
	@GetMapping("/list/{productId}/{page}") // GET : 댓글(문의) 선택(all)  // 나중에 데이터 받는 거에 따라 달라짐
	public ResponseEntity<Map<String, Object>> readAllInquiry(
			Pagination pagination,
			@PathVariable("productId") int productId,
			@PathVariable("page") int page){
		log.info("readAllInquiry()");
		
		pagination.setPageNum(page);
		log.info("pagination : " + pagination);
		// 문자열 형태와 오브젝트 형태를 저장 하기 위해서 사용
		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		// 설정 된 기본 페이지 적용
		pageMaker.setPagination(pagination);	
		log.info("쪽수 기본값 : " + pageMaker.getPagination());
		
		// productId에 해당하는 댓글(리뷰) list을 전체 검색
		List<InquiryVO> inquiryList = inquiryService.getAllInquiryPaging(productId, pageMaker);
		
		log.info("inquiryList : " + inquiryList);
		resultMap.put("inquiryList", inquiryList);
		resultMap.put("pageMaker", pageMaker);
		
		// list값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}// end readAllInquiry()
	
//	@GetMapping("/AllList/{page}") // GET : 댓글(문의) 선택(all)  // 나중에 데이터 받는 거에 따라 달라짐
//	public ResponseEntity<Map<String, Object>> readAllInquiry(
//			Pagination pagination,
//			@PathVariable("page") int page){
//		log.info("readAllInquiry()");
//		
//		pagination.setPageNum(page);
//		log.info("pagination : " + pagination);
//		// 문자열 형태와 오브젝트 형태를 저장 하기 위해서 사용
//		Map<String, Object> resultMap = new HashMap<>();
//		PageMaker pageMaker = new PageMaker();
//		// 설정 된 기본 페이지 적용
//		pageMaker.setPagination(pagination);	
//		log.info("쪽수 기본값 : " + pageMaker.getPagination());
//		
//		// productId에 해당하는 댓글(리뷰) list을 전체 검색
//		List<InquiryVO> inquiryList = inquiryService.getAllInquiryPaging(productId, pageMaker);
//		
//		log.info("inquiryList : " + inquiryList);
//		resultMap.put("inquiryList", inquiryList);
//		resultMap.put("pageMaker", pageMaker);
//		
//		// list값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
//		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
//	}// end readAllInquiry()
	
	@PreAuthorize("#inquiryVO.memberId == authentication.principal.username")
	@PutMapping("/modify") // PUT : 댓글(리뷰) 수정 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> updateInquiry(
			 @RequestBody InquiryVO inquiryVO
	         ){	
	      log.info("updateInquiry()");
	      log.info("inquiryVO() : " + inquiryVO);
	      int productId = inquiryVO.getProductId();
	      String memberId = inquiryVO.getMemberId();
	      String inquiryContent = inquiryVO.getInquiryContent();
	      
	      // reviewId에 해당하는 댓글(리뷰)의 reviewContent, reviewStar, imgId의 내용을 수정 할 수 있습니다.
	      int result = inquiryService.updateInquiry(productId, memberId, inquiryContent);
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateInquiry()
	
	@PreAuthorize("#inquiryVO.memberId == authentication.principal.username")
	@DeleteMapping("/delete") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> deleteInquiry(
			   @RequestBody InquiryVO inquiryVO) {
	      log.info("deleteInquiry()");
	      
	      int productId = inquiryVO.getProductId();
	      String memberId = inquiryVO.getMemberId();
	      
	      // productId에 해당하는 reviewId의 댓글(리뷰)
	      int result = inquiryService.deleteInquiry(productId, memberId);
	
	      log.info(result + "행 댓글 삭제");
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end deleteInquiry()
	
	@GetMapping("/myList")
	public ResponseEntity<Map<String, Object>> myListInquiryGET(Pagination pagination,
			@AuthenticationPrincipal MemberDetails memberDetails) {
		log.info("myListInquiry()");
		String memberId = memberDetails.getUsername();
		log.info("pagination : " + pagination);
		
		Map<String, Object> resultMap = new HashMap<>();
		
		//페이지 메이커에 기본 쪽수값 저장
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		log.info("쪽수 기본값 : " + pageMaker.getPagination());
		
//		List<InquiryVO> listInquiry = inquiryService.getAllInquiryMemberIdPaging(memberId, pageMaker);
		List<InquiryDTO> listInquiry = inquiryService.getAllMyInquiryDTO(memberId, pageMaker);
		
		log.info("listInquiry : " + listInquiry);
		log.info("pageMaker : " + pageMaker);
		
		resultMap.put("listInquiry", listInquiry);
		resultMap.put("pageMaker", pageMaker);
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}// myListInquiryGET 
}
