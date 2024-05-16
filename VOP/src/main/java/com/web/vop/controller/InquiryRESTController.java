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
@RequestMapping("설정 예정1")
@Log4j
public class InquiryRESTController {
	
	@Autowired
	private InquiryService inquiryService;
	
	@PostMapping("/register") // POST : 댓글(문의) 입력
	public ResponseEntity<Integer> createInquiry(@RequestBody InquiryVO inquiryVO){
		log.info("createInquiry()");
		
		// inquiryVO 입력 받아 댓글(문의) 등록
		int result = inquiryService.createInquiry(inquiryVO);
		
		log.info(result + "행 댓글 등록");
		// result값을 전송하여 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createInquiry()
	
	
	@GetMapping("설정 예정") // GET : 댓글(문의) 선택(all)  // 나중에 데이터 받는 거에 따라 달라짐
	public ResponseEntity<List<InquiryVO>> readAllInquiry(
			@PathVariable("productId") int productId){
		log.info("readAllInquiry()");
		
		// productId에 해당하는 댓글(리뷰) list을 전체 검색
		List<InquiryVO> list = inquiryService.getAllInquiry(productId);
		
		// list값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<List<InquiryVO>>(list, HttpStatus.OK);
	}// end readAllInquiry()
	
	@PutMapping("/{productId}/{memberId}") // PUT : 댓글(리뷰) 수정 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> updateInquiry(
	         @PathVariable("productId") int reviewId,
	         @PathVariable("memberId") String memberId,
	         @RequestBody String  inquiryContent
	         ){	
	      log.info("updateInquiry()");
	      
	      // replyId 확인 로그
	      log.info("reviewId = " + reviewId);
	      
	      // reviewId에 해당하는 댓글(리뷰)의 reviewContent, reviewStar, imgId의 내용을 수정 할 수 있습니다.
	      int result = inquiryService.updateInquiry(reviewId, memberId, inquiryContent);
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateInquiry()
	
	@DeleteMapping("/{productId}/{memberId}") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> deleteInquiry(
	         @PathVariable("productId") int productId,
	         @PathVariable("memberId") String memberId) {
	      log.info("deleteInquiry()");
	      
	      // productId에 해당하는 reviewId의 댓글(리뷰)
	      int result = inquiryService.deleteInquiry(productId, memberId);
	
	      log.info(result + "행 댓글 삭제");
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end deleteInquiry()
	
}
