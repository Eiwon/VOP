package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ProductService;
import com.web.vop.service.ReviewService;


import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/review")
@Log4j
public class ReviewRESTController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProductService productService;

	
	    @PostMapping("/register") // POST : 댓글(리뷰) 입력
	    public ResponseEntity<Integer> createReviewPOST(@RequestBody ReviewVO reviewVO) {

	        log.info("createReview()");
	        log.info("reviewVO : " + reviewVO);

	        int productId = reviewVO.getProductId();
	        String memberId = reviewVO.getMemberId();

	        log.info("productId :" + productId);
	        log.info("memberId : " + memberId);
	        
	        ProductVO productVO = productService.getProductById(productId);
	        
	        int res = 0;
	        
	        if(productVO != null) {
	        res = 2;
	        // 해당 상품에 회원이 댓글을 작성했는지 확인하기 위해 댓글 검색
	        ReviewVO vo = reviewService.selectByReview(productId, memberId);
	        log.info("vo : " + vo);

	        // 해당 상품에 회원이 댓글(리뷰)를 작성하지 않았으면 작성 가능, 아니면 작성 불가
	        if (vo == null) {
	        	
	        	 res = reviewService.createReview(reviewVO); // imgDetails 넣어야 함
	          
	        } else {
	            log.info(memberId + "님은 " + productId + "상품 번호에 이미 댓글(리뷰)를 등록 하였습니다.");
	        }
	        
	       }

	        // result 값을 전송하여 리턴하는 방식으로 성공하면 200 OK를 전송합니다.
	        return new ResponseEntity<>(res, HttpStatus.OK);
	    }
	
	
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

}
