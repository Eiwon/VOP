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
@RequestMapping("설정 예정2")
@Log4j
public class AnswerRESTController {
	
	@Autowired
	private AnswerService answerService;
	
	// 댓댓글(답변) 등록	
	@PostMapping("/register") // POST : 댓댓글(답변) 입력
	public ResponseEntity<Integer> createAnswer(@RequestBody AnswerVO answerVO){
		log.info("createAnswer()");
		
		// inquiryVO 입력 받아 댓댓글(답변) 등록
		int result = answerService.createAnswer(answerVO);
		
		log.info(result + "행 댓댓글(답변) 등록");
		// result값을 전송하여 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createAnswer()
	
	// 댓댓글(답변) 전체 검색
//	@GetMapping("설정 예정") // GET : 댓댓글(답변) 선택(all)  // 나중에 데이터 받는 거에 따라 달라짐
//	public ResponseEntity<List<AnswerVO>> readAllAnswer(
//			@PathVariable("productId") int productId){
//		log.info("readAllAnswer()");
//		
//		// productId에 해당하는 댓댓글(답변) list을 전체 검색
//		List<AnswerVO> list = answerService.getAllAnswer(productId);
//		
//		// list값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
//		return new ResponseEntity<List<AnswerVO>>(list, HttpStatus.OK);
//	}// end readAllAnswer()
	
	// 댓댓글(답변) 수정
	@PutMapping("/{productId}/{memberId}") // PUT : 댓글(리뷰) 수정 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> updateAnswer(
	         @PathVariable("productId") int productId,
	         @PathVariable("memberId") String memberId,
	         @RequestBody String  answerContent
	         ){	
	      log.info("updateAnswer()");
	      
	      // reviewId에 해당하는 댓글(리뷰)의 reviewContent, reviewStar, imgId의 내용을 수정 할 수 있습니다.
	      int result = answerService.updateAnswer(productId, memberId, answerContent);
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateAnswer()
	
	// 댓댓글(답변) 삭제
	@DeleteMapping("/{productId}/{memberId}") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> deleteAnswer(
	         @PathVariable("productId") int productId,
	         @PathVariable("memberId") String memberId) {
	      log.info("deleteAnswer()");
	      
	      // productId에 해당하는 reviewId의 댓글(리뷰)
	      int result = answerService.deleteAnswer(productId, memberId);
	
	      log.info(result + "행 댓글 삭제");
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end deleteInquiry()

}
