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
@RequestMapping("inquiry")
@Log4j
public class InquiryRESTController {
	
	@Autowired
	private InquiryService inquiryService;
	
	@PostMapping("/register") // POST : 댓글(문의) 입력
	public ResponseEntity<Integer> createInquiry(@RequestBody InquiryVO inquiryVO){
		log.info("createInquiry()");
		log.info("inquiryVO : " + inquiryVO);
		
		// inquiryVO 입력 받아 댓글(문의) 등록
		int result = inquiryService.createInquiry(inquiryVO);
		
		log.info(result + "행 문의 등록");
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
	
	@PutMapping("/modify") // PUT : 댓글(리뷰) 수정 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> updateInquiry(
			 @RequestBody InquiryVO inquiryVO
	         ){	
	      log.info("updateInquiry()");
	      
	      log.info("inquiryVO() : " + inquiryVO);
	      
	      // reviewId에 해당하는 댓글(리뷰)의 reviewContent, reviewStar, imgId의 내용을 수정 할 수 있습니다.
	      int result = inquiryService.updateInquiry(inquiryVO.getProductId(), inquiryVO.getMemberId(), inquiryVO.getInquiryContent());
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateInquiry()
	
	@DeleteMapping("/delete") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> deleteInquiry(
			   @RequestBody InquiryVO inquiryVO) {
	      log.info("deleteInquiry()");
	      
	      log.info("productId : " + inquiryVO.getProductId());
	      log.info("memberId : " + inquiryVO.getMemberId());
	      
	      // productId에 해당하는 reviewId의 댓글(리뷰)
	      int result = inquiryService.deleteInquiry(inquiryVO.getProductId(), inquiryVO.getMemberId());
	
	      log.info(result + "행 댓글 삭제");
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end deleteInquiry()
	
}
