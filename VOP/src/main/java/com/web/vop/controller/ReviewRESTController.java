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



import com.web.vop.domain.ReviewVO;

import com.web.vop.service.ReviewService;


import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/review")
@Log4j
public class ReviewRESTController {
	
	@Autowired
	private ReviewService reviewService;

	
	 @Transactional(value = "transactionManager") // ��� �Է��� ��� �� ���� ���� �� ������ ��հ� ����
	    @PostMapping("/register") // POST : ���(����) �Է�
	    public ResponseEntity<Integer> createReviewPOST(@RequestBody ReviewVO reviewVO) {

	        log.info("createReview()");
	        log.info("reviewVO : " + reviewVO);

	        int productId = reviewVO.getProductId();
	        String memberId = reviewVO.getMemberId();

	        log.info("productId :" + productId);
	        log.info("memberId : " + memberId);

	        int res = 0;

	        // �ش� ��ǰ�� ȸ���� ����� �ۼ��ߴ��� Ȯ���ϱ� ���� ��� �˻�
	        ReviewVO vo = reviewService.selectByReview(productId, memberId);
	        log.info("vo : " + vo);

	        // �ش� ��ǰ�� ȸ���� ���(����)�� �ۼ����� �ʾ����� �ۼ� ����, �ƴϸ� �ۼ� �Ұ�
	        if (vo == null) {
	        	
	        	 res = reviewService.createReview(reviewVO); // imgDetails �־�� ��
	          
	        } else {
	            log.info(memberId + "���� " + productId + "��ǰ ��ȣ�� �̹� ���(����)�� ��� �Ͽ����ϴ�.");
	        }

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
	
	 @Transactional(value = "transactionManager")// ��� ���� �� ������ ��� ���� ����
	 @PutMapping("/modify") // PUT : ���(����) ���� 
	   public ResponseEntity<Integer> updateReview(
	         @RequestBody ReviewVO reviewVO
	         ){
	      log.info("updateReview()");
	      
//	  	  // �Ҽ��� ù ° �ڸ������� ���
//	      DecimalFormat df = new DecimalFormat("#.#");
	      
	      int reviewId = reviewVO.getReviewId();
	      	
	      float reviewStar = reviewVO.getReviewStar();
	      
	      String reviewContent = reviewVO.getReviewContent();
	      
	      int productId = reviewVO.getProductId();
	      
	      log.info("productId : " + productId);
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = reviewService.updateReview(reviewId, reviewContent, reviewStar, productId);
	      
//	      // productId�� �ش��ϴ� ��ǰ ��ȸ // ���׷��̵� �� ����
//		  ProductVO productVO = productService.getProductById(productId);
//	      
//	      int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
//		  String reviewAvg = "0";
//	      
//	      if(result == 1) {
//	    	    // ���� �� ��
//				res = productService.selectReviewStar(productId);
//				log.info("����(��) : " + res);
//				
//				// ���� ��� �� reviewStar
//				reviewAvg = df.format((float)res / productVO.getReviewNum());
//				
//				log.info("res : " + res);
//				log.info("reviewAvg : " + reviewAvg);
//				
//				// ���� ��հ� ������Ʈ
//				int updateRes = productService.updateReviewAvg(productId, reviewAvg);
//
//				log.info("updateRes : " + updateRes);
//				
//				log.info(result + "�� ���� �Ǿ����ϴ�.");
//	      } 
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateReview()

}
