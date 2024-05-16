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
		log.info("register ������ �̵� ��û");
	} // end loginGET
	
	@PostMapping("/register") // POST : ���(����) �Է�
	public ResponseEntity<Integer> createReview(@RequestBody ReviewVO reviewVO){
		log.info("createReview()");
		
		// reviewVO �Է� �޾� ���(����) ���
		int result = reviewService.createReview(reviewVO);
		
		// ����� �Ϸ� �Ǿ����� ��� �� ���� ����
		if(result == 1) {
			// ��ǰ ���̺� ��� �� ���� ����
			int res = reviewService.reviewNumUP(reviewVO.getProductId());
			log.info(res + "�� ��� ����");
		}
		
		log.info(result + "�� ��� ���");
		// result���� �����Ͽ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
//	// productController������ �̵�
//	// ��� ../all �ΰ� ����.
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
	}
	
	
	 @PutMapping("/{reviewId}") // PUT : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> updateReview(
	         @PathVariable("reviewId") int reviewId,
	         @RequestBody String reviewContent,
	         @RequestBody float reviewStar
	         ){
	      log.info("updateReply()");
	      
	      // replyId Ȯ�� �α�
	      log.info("reviewId = " + reviewId);
	      
	      // reviewId�� �ش��ϴ� ���(����)�� reviewContent, reviewStar, imgId�� ������ ���� �� �� �ֽ��ϴ�.
	      int result = reviewService.updateReview(reviewId, reviewContent, reviewStar);
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }
	 
	 @DeleteMapping("/{reviewId}/{productId}") // DELETE : ���(����) ���� // ���߿� ������ �޴� �ſ� ���� �޶���
	   public ResponseEntity<Integer> deleteReview(
	         @PathVariable("reviewId") int reviewId,
	         @PathVariable("productId") int productId) {
	      log.info("deleteReview()");
	      // reviewId Ȯ�� �α�
	      log.info("reviewId = " + reviewId);
	      
	      // productId�� �ش��ϴ� reviewId�� ���(����)
	      int result = reviewService.deleteReview(reviewId, productId);
	      
	      // ������ �Ϸ� �Ǿ����� ��� �� ���� ����
	      if(result == 1) {
	    	  // ��ǰ ���̺� ��� �� ���� ����
			  int res = reviewService.reviewNumDown(productId);
			  log.info(res + "�� ��� ����");
			}
	      
	      log.info(result + "�� ��� ����");
	      
	      // result���� �����ϰ� �����ϴ� ������� �����ϸ� 200 ok�� �����ϴ�.
	      return new ResponseEntity<Integer>(result, HttpStatus.OK);
	   }

	
}
