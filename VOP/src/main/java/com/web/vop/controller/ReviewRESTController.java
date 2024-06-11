package com.web.vop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.domain.AnswerVO;
import com.web.vop.domain.MessageVO;

import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ProductService;
import com.web.vop.service.ReviewService;
import com.web.vop.socket.AlarmHandler;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/review")
@Log4j
public class ReviewRESTController {
	
	@Autowired
	private ReviewService reviewService;
	
	// 댓글 알림을 보내기 위한 알람핸들러
	@Autowired
	public WebSocketHandler alarmHandler;
	
	@PostMapping("/register") // POST : 댓글(리뷰) 입력
	public ResponseEntity<Integer> createReviewPOST(@RequestBody ReviewVO reviewVO) {

		log.info("createReview()");
		log.info("reviewVO : " + reviewVO);

		int res = reviewService.createReview(reviewVO); // imgDetails 넣어야 함

		if(res == 1) {
			// 댓글 알람 송신
	        ((AlarmHandler)alarmHandler).sendReplyAlarm(reviewVO.getProductId());
		}
		
		// result 값을 전송하여 리턴하는 방식으로 성공하면 200 OK를 전송합니다.
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	
	@GetMapping("/all/{productId}/{page}") // GET : 댓글(리뷰) 선택(all)
	public ResponseEntity<Map<String, Object>> readAllReview(
			@ModelAttribute Pagination pagination,
			@PathVariable("productId") int productId,
			@PathVariable("page") int page
			){
		log.info("readAllReview()");
		
		pagination.setPageNum(page);
		log.info("pagination : " + pagination);
		Map<String, Object> resultMap = new HashMap<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);	
		log.info("쪽수 기본값 : " + pageMaker.getPagination());
		
		// productId에 해당하는 댓글(리뷰) list을 전체 검색
		List<ReviewVO> list = reviewService.getAllReviewPaging(productId, pageMaker);
		
		log.info("list : " + list);
		log.info("pageMaker : " + pageMaker);
		
		resultMap.put("list", list);
		resultMap.put("pageMaker", pageMaker);
		
		// list값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}// end readAllReview()
	
	
	 @PutMapping("/modify") // PUT : 댓글(리뷰) 수정 
	   public ResponseEntity<Integer> updateReview(
	         @RequestBody ReviewVO reviewVO
	         ){
	      log.info("updateReview()");
	      
	      int reviewId = reviewVO.getReviewId();
	      	
	      float reviewStar = reviewVO.getReviewStar();
	      
	      String reviewContent = reviewVO.getReviewContent();
	      
	      int productId = reviewVO.getProductId();
	      
	      log.info("productId : " + productId);
	      
	      // reviewId에 해당하는 댓글(리뷰)의 reviewContent, reviewStar, imgId의 내용을 수정 할 수 있습니다.
	      int result = reviewService.updateReview(reviewId, reviewContent, reviewStar, productId);

	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateReview()
	 
	 @DeleteMapping("/delete")
	 public ResponseEntity<Integer> deleteReview(
			 @RequestBody ReviewVO reviewVO){
		 log.info("deleteReview()");
		 
		 int productId = reviewVO.getProductId();
	      
	     String memberId = reviewVO.getMemberId();
	     
	     int result = reviewService.deleteReview(productId, memberId);
	     
	     log.info(result + "행 댓글 삭제");
	     
	     return new ResponseEntity<Integer>(result, HttpStatus.OK);
	 }

}
