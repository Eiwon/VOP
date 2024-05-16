package com.web.vop.controller;

import java.util.List;

import javax.swing.ListModel;

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

import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ReviewService;

import lombok.CustomLog;
import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/review")
@Log4j
public class ReviewRESTController {
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/register")
	public void loginGET() {
		log.info("register 페이지 이동 요청");
	} // end loginGET
	
	@PostMapping("/register") // POST : 댓글(리뷰) 입력
	public ResponseEntity<Integer> createReview(@RequestBody ReviewVO reviewVO){
		log.info("createReview()");
		
		// reviewVO 입력 받아 댓글(리뷰) 등록
		int result = reviewService.createReview(reviewVO);
		
		// 등록이 완료 되었을때 댓글 총 갯수 증가
		if(result == 1) {
			// 상품 테이블에 댓글 총 갯수 증가
			int res = reviewService.reviewNumUP(reviewVO.getProductId());
			log.info(res + "행 댓글 증가");
		}
		
		log.info(result + "행 댓글 등록");
		// result값을 전송하여 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
//	// productController쪽으로 이동
//	// 경로 ../all 인것 같다.
	@GetMapping("/all/{productId}") // GET : 댓글(리뷰) 선택(all)
	public ResponseEntity<List<ReviewVO>> readAllReview(
			@PathVariable("productId") int productId){
		log.info("readAllReview()");
		
		// productId 확인 로그
		log.info("productId = " + productId);
		
		// productId에 해당하는 댓글(리뷰) list을 전체 검색
		List<ReviewVO> list = reviewService.getAllReview(productId);
		
		// list값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<List<ReviewVO>>(list, HttpStatus.OK);
	}
	
	
	 @PutMapping("/{reviewId}") // PUT : 댓글(리뷰) 수정 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> updateReview(
	         @PathVariable("reviewId") int reviewId,
	         @RequestBody String reviewContent,
	         @RequestBody float reviewStar
	         ){
	      log.info("updateReply()");
	      
	      // replyId 확인 로그
	      log.info("reviewId = " + reviewId);
	      
	      // reviewId에 해당하는 댓글(리뷰)의 reviewContent, reviewStar, imgId의 내용을 수정 할 수 있습니다.
	      int result = reviewService.updateReview(reviewId, reviewContent, reviewStar);
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }
	 
	 @DeleteMapping("/{reviewId}/{productId}") // DELETE : 댓글(리뷰) 삭제 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> deleteReview(
	         @PathVariable("reviewId") int reviewId,
	         @PathVariable("productId") int productId) {
	      log.info("deleteReview()");
	      // reviewId 확인 로그
	      log.info("reviewId = " + reviewId);
	      
	      // productId에 해당하는 reviewId의 댓글(리뷰)
	      int result = reviewService.deleteReview(reviewId, productId);
	      
	      // 삭제가 완료 되었을때 댓글 총 갯수 감소
	      if(result == 1) {
	    	  // 상품 테이블에 댓글 총 갯수 감소
			  int res = reviewService.reviewNumDown(productId);
			  log.info(res + "행 댓글 감소");
			}
	      
	      log.info(result + "행 댓글 삭제");
	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }

	
}
