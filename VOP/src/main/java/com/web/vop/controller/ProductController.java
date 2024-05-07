package com.web.vop.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ProductService;
import com.web.vop.service.ReviewService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/product")
@Log4j
public class ProductController {
	
	@Autowired
	// ProductService Ŭ������ �ִ� ����� ����ϱ����� ����
	private ProductService productService;
	
	@Autowired
	// ReviewService Ŭ������ �ִ� ����� ����ϱ����� ����
	private ReviewService reviewService;
	
	// ��ǰ �� ���� ��ȸ
	@GetMapping("/detail")
	public void productDetailGET(Model model, Integer productId) {
		log.info("productDetailGET()");
		log.info("productId : " + productId);
		
		// productId�� �ش��ϴ� ��ǰ ��ȸ 
		ProductVO productVO = productService.getProductById(productId);
		log.info("/product/detail get");
		model.addAttribute("productVO", productVO);
	} // end productDetail()
	
//	// ÷�� ���� �̹��� �� ���� ��ȸ(GET)
//    //@GetMapping("/product/detail")
//    public void detailGET(int productId, Model model) {
//        log.info("detailGET()");
//        log.info("productId : " + productId);
//        
//        // imgId�� �� ���� ��ȸ
//        ProductVO ProductVO = productService.selectByMainImg(productId);
//        
//        log.info("/product/detail get");
//        // ��ȸ�� �� ������ Model�� �߰��Ͽ� ����
//        model.addAttribute("ProductVO", ProductVO);
//    } // end detail()
    
	// ��� �� ���� ��ȸ
	@GetMapping("/detail")
	public void reviewCountGET(Model model, Integer productId) {
		log.info("reviewCountGET()");
		int reviewCount = productService.selectReviewByCount(productId);
		log.info("reviewCount : " + reviewCount);
		model.addAttribute("reviewCount", reviewCount);
	}
	
	// ��ǰ ����(��) �� �� �˻�
	public void reviewStarGET(Model model, Integer productId) {
		log.info("reviewStarGET()");
		int res = productService.selectReviewByStar(productId);
		int reviewCount = productService.selectReviewByCount(productId);
		float reviewStar = res / reviewCount;
		model.addAttribute("reviewStar", reviewStar);
	}
	
	 // ��� ��ü ��ȸ
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
	
	
	@GetMapping("/register")
	public void registerGET() {
		log.info("registerGET()");
	} // end productRegister()
	
	@PostMapping("/register")
	public void registerPOST(ProductVO productVO, MultipartFile file) {
		log.info("registerPOST()");
		log.info(productVO);
		log.info("���� �� : " + file.getOriginalFilename());
		
	} // end registerPOST
	
	
  
	
}
