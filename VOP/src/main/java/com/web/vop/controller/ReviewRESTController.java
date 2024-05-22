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
	
	@PostMapping("/register") // POST : ���(����) �Է�
	public ResponseEntity<Integer> createReviewPOST(@RequestBody ReviewVO reviewVO){
		log.info("createReview()");
		
		// �Ҽ��� ù ° �ڸ������� ���
		DecimalFormat df = new DecimalFormat("#.#");
	
		log.info("reviewVO : " + reviewVO);
		
		int productId = reviewVO.getProductId();
		
		String memberId = reviewVO.getMemberId();
		
		log.info("productId :" + productId);
		log.info("memberId : " + memberId);
		
		int result = 0;
		
		// ��� ��ü �˻�
		ReviewVO vo = reviewService.selectByReview(productId, memberId);
			
		log.info("vo : " + vo);
		if(vo == null) {
			// reviewVO �Է� �޾� ���(����) ���
			result = reviewService.createReview(reviewVO);
			
			// ����� �Ϸ� �Ǿ����� ��� �� ���� ����
			if(result == 1) {
				
				// ���� ��ǰ ��� ī���� 
				int reviewNum =  productService.selectReviewByCount(productId);
				
				// ��� �� ���� �α�
				log.info("reviewNum : " + reviewNum);
				
				// ��ǰ ��� ī���� ����
				int updateRes = productService.updateReviewNum(productId, reviewNum);
				
				// ���� ��� ���� �ڵ�
				// productId�� �ش��ϴ� ��ǰ ��ȸ // ���׷��̵� �� ����
				ProductVO productVO = productService.getProductById(productId);
				
				int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
				String reviewAvg = "0";
				if(productVO.getReviewNum() != 0) { //0 ������ �� ������ ������ ���Ϳ´�.
					// ���� �� ��
					res = productService.selectReviewStar(productId);
					log.info("����(��) : " + res);
					
					// ���� ��� �� reviewStar
					reviewAvg = df.format((float)res / productVO.getReviewNum());
					
					log.info("res : " + res);
					log.info("reviewAvg : " + reviewAvg);
					
					// ���� ��հ� ������Ʈ
					updateRes = productService.updateReviewAvg(productId, reviewAvg);

					log.info("updateRes : " + updateRes);
				}
			}

			log.info(result + "�� ��� ���");
		} else {
			log.info(memberId + "���� " + productId + "��ǰ ��ȣ�� �̹� ���(����)�� ��� �Ͽ����ϴ�.");
		}
		
		// result���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}// end createReviewPOST()
	
	
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
	
	 @PutMapping("/modify") // PUT : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> updateReview(
	         @RequestBody ReviewVO reviewVO
	         ){
	      log.info("updateReview()");
	      
	      log.info("�α�()");
	      
	  	  // �Ҽ��� ù ° �ڸ������� ���
	      DecimalFormat df = new DecimalFormat("#.#");
	      
	      int reviewId = reviewVO.getReviewId();
	      
	      float reviewStar = reviewVO.getReviewStar();
	      
	      String reviewContent = reviewVO.getReviewContent();
	      
	      int productId = reviewVO.getProductId();
	      
	      log.info("productId : " + productId);
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = reviewService.updateReview(reviewId, reviewContent, reviewStar);
	      
	      // productId�� �ش��ϴ� ��ǰ ��ȸ // ���׷��̵� �� ����
		  ProductVO productVO = productService.getProductById(productId);
	      
	      int res = 0; // ��� �Է½� �Ҽ��� �Է� �Ұ�
		  String reviewAvg = "0";
	      
	      if(result == 1) {
	    	    // ���� �� ��
				res = productService.selectReviewStar(productId);
				log.info("����(��) : " + res);
				
				// ���� ��� �� reviewStar
				reviewAvg = df.format((float)res / productVO.getReviewNum());
				
				log.info("res : " + res);
				log.info("reviewAvg : " + reviewAvg);
				
				// ���� ��հ� ������Ʈ
				int updateRes = productService.updateReviewAvg(productId, reviewAvg);

				log.info("updateRes : " + updateRes);
				
				log.info(result + "�� ���� �Ǿ����ϴ�.");
	      } 

	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }// end updateReview()

}
