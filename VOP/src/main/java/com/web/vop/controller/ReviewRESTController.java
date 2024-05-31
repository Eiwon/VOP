package com.web.vop.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import com.web.vop.domain.MessageVO;

import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ProductService;
import com.web.vop.service.ReviewService;
import com.web.vop.socket.AlarmHandler;


import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/review")
@Log4j
public class ReviewRESTController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProductService productService;

	
	    @PostMapping("/register") // POST : ���(����) �Է�
	    public ResponseEntity<Integer> createReviewPOST(@RequestBody ReviewVO reviewVO) {

	        log.info("createReview()");
	        log.info("reviewVO : " + reviewVO);
	        
	        int res = reviewService.createReview(reviewVO); // imgDetails �־�� ��

	        // result ���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 OK�� �����մϴ�.
	        return new ResponseEntity<>(res, HttpStatus.OK);
	    }
	
	
	@GetMapping("/all/{productId}") // GET : ���(����) ����(all)
	public ResponseEntity<List<ReviewVO>> readAllReview(
			@PathVariable("productId") int productId){
		log.info("readAllReview()");
		
		// productId Ȯ�� �α�
		log.info("productId = " + productId);
		
		// productId�� �ش��ϴ� ���(����) list�� ��ü �˻�
		List<ReviewVO> list = reviewService.getAllReview(productId);
		
		// list���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<List<ReviewVO>>(list, HttpStatus.OK);
	}// end readAllReview()
	
	
	 @PutMapping("/modify") // PUT : ���(����) ���� 
	   public ResponseEntity<Integer> updateReview(
	         @RequestBody ReviewVO reviewVO
	         ){
	      log.info("updateReview()");
	      
	      int reviewId = reviewVO.getReviewId();
	      	
	      float reviewStar = reviewVO.getReviewStar();
	      
	      String reviewContent = reviewVO.getReviewContent();
	      
	      int productId = reviewVO.getProductId();
	      
	      log.info("productId : " + productId);
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = reviewService.updateReview(reviewId, reviewContent, reviewStar, productId);

	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateReview()
	 
	 @DeleteMapping("/delete")
	 public ResponseEntity<Integer> deleteReview(
			 @RequestBody ReviewVO reviewVO){
		 log.info("deleteReview()");
		 
		 int productId = reviewVO.getProductId();
	      
	     String memberId = reviewVO.getMemberId();
	     
	     int result = reviewService.deleteReview(productId, memberId);
	     
	     log.info(result + "�� ��� ����");
	     
	     return new ResponseEntity<Integer>(result, HttpStatus.OK);
	 }

}
