package com.web.vop.controller;

import java.text.DecimalFormat;
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
	public ResponseEntity<Integer> createReviewPOST(@RequestBody ReviewVO reviewVO){
		log.info("createReview()");
		
		// 소수점 첫 째 자리까지만 출력
		DecimalFormat df = new DecimalFormat("#.#");
	
		log.info("reviewVO : " + reviewVO);
		
		int productId = reviewVO.getProductId();
		
		String memberId = reviewVO.getMemberId();
		
		log.info("productId :" + productId);
		log.info("memberId : " + memberId);
		
		int result = 0;
		
		// 댓글 전체 검색
		ReviewVO vo = reviewService.selectByReview(productId, memberId);
			
		log.info("vo : " + vo);
		if(vo == null) {
			// reviewVO 입력 받아 댓글(리뷰) 등록
			result = reviewService.createReview(reviewVO);
			
			// 등록이 완료 되었을때 댓글 총 갯수 증가
			if(result == 1) {
				
				// 현재 상품 댓글 카운터 
				int reviewNum =  productService.selectReviewByCount(productId);
				
				// 댓글 총 갯수 로그
				log.info("reviewNum : " + reviewNum);
				
				// 상품 댓글 카운터 수정
				int updateRes = productService.updateReviewNum(productId, reviewNum);
				
				// 리뷰 평균 관련 코드
				// productId에 해당하는 상품 조회 // 업그레이드 된 상태
				ProductVO productVO = productService.getProductById(productId);
				
				int res = 0; // 댓글 입력시 소수점 입력 불가
				String reviewAvg = "0";
				if(productVO.getReviewNum() != 0) { //0 이하일 때 무한의 에러가 나와온다.
					// 리뷰 총 합
					res = productService.selectReviewStar(productId);
					log.info("리뷰(별) : " + res);
					
					// 리뷰 평균 값 reviewStar
					reviewAvg = df.format((float)res / productVO.getReviewNum());
					
					log.info("res : " + res);
					log.info("reviewAvg : " + reviewAvg);
					
					// 리뷰 평균값 업데이트
					updateRes = productService.updateReviewAvg(productId, reviewAvg);

					log.info("updateRes : " + updateRes);
				}
			}

			log.info(result + "행 댓글 등록");
		} else {
			log.info(memberId + "님은 " + productId + "상품 번호에 이미 댓글(리뷰)를 등록 하였습니다.");
		}
		
		// result값을 전송하여 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createReviewPOST()
	
	
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
	
	 @PutMapping("/modify") // PUT : 댓글(리뷰) 수정 // 나중에 데이터 받는 거에 따라 달라짐
	   public ResponseEntity<Integer> updateReview(
	         @RequestBody ReviewVO reviewVO
	         ){
	      log.info("updateReview()");
	      
	      log.info("로그()");
	      
	  	  // 소수점 첫 째 자리까지만 출력
	      DecimalFormat df = new DecimalFormat("#.#");
	      
	      int reviewId = reviewVO.getReviewId();
	      
	      float reviewStar = reviewVO.getReviewStar();
	      
	      String reviewContent = reviewVO.getReviewContent();
	      
	      int productId = reviewVO.getProductId();
	      
	      log.info("productId : " + productId);
	      
	      // reviewId에 해당하는 댓글(리뷰)의 reviewContent, reviewStar, imgId의 내용을 수정 할 수 있습니다.
	      int result = reviewService.updateReview(reviewId, reviewContent, reviewStar);
	      
	      // productId에 해당하는 상품 조회 // 업그레이드 된 상태
		  ProductVO productVO = productService.getProductById(productId);
	      
	      int res = 0; // 댓글 입력시 소수점 입력 불가
		  String reviewAvg = "0";
	      
	      if(result == 1) {
	    	    // 리뷰 총 합
				res = productService.selectReviewStar(productId);
				log.info("리뷰(별) : " + res);
				
				// 리뷰 평균 값 reviewStar
				reviewAvg = df.format((float)res / productVO.getReviewNum());
				
				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);
				
				// 리뷰 평균값 업데이트
				int updateRes = productService.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + updateRes);
				
				log.info(result + "행 수정 되었습니다.");
	      } 

	      
	      // result값을 전송하고 리턴하는 방식으로 성공하면 200 ok를 갔습니다.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateReview()

}
