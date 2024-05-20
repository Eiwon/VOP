package com.web.vop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ImageService;
import com.web.vop.service.ProductService;
import com.web.vop.service.ReviewService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/review")
@Log4j
public class ReviewController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProductService productService;
	
	// 댓글(리뷰) 등록 GET
	@GetMapping("/register")
	public void createReviewGET(Model model, Integer productId, Integer imgId) {
		log.info("createReviewGET()");
		
		log.info("productId : " + productId);
		log.info("imgId : " + imgId);
		
		

		ImageVO imageVO = imageService.getImageById(imgId);
		
		String imgRealName = imageVO.getImgRealName();
		String imgExtension = imageVO.getImgExtension();
		
		log.info("imageVO : " + imageVO);
		
		log.info("imgRealName : " + imgRealName);
		log.info("imgExtension : " + imgExtension);
		
		model.addAttribute("imgRealName", imgRealName);
		model.addAttribute("productId", productId);
		model.addAttribute("imgId", imgId);
		model.addAttribute("imgExtension", imgExtension);
	} // end loginGET
	
	
	// 댓글(리뷰) 수정 GET
	@GetMapping("/modify")
	public void updateReviewGET(Model model, Integer productId, Integer imgId, String memberId) {
		log.info("updateReviewGET()");
		
		log.info("productId : " + productId);
		log.info("imgId : " + imgId);
		
		ReviewVO reviewVO = reviewService.selectByReview(productId, memberId);
		int reviewId = ((ReviewVO) reviewVO).getReviewId();

		ImageVO imageVO = imageService.getImageById(imgId);
		
		String imgRealName = imageVO.getImgRealName();
		String imgExtension = imageVO.getImgExtension();
		
		log.info("imageVO : " + imageVO);
		
		log.info("imgRealName : " + imgRealName);
		log.info("imgExtension : " + imgExtension);
		
		model.addAttribute("productId", productId);
		model.addAttribute("imgRealName", imgRealName);
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("imgId", imgId);
		model.addAttribute("imgExtension", imgExtension);
	} // end loginGET
	
//	// 댓글(리뷰) 전체 검색 GET
	@GetMapping("/list") // GET : 댓글(리뷰) 선택(all)
	public void readAllReviewMemberId(Model model, String memberId){
		log.info("readAllReviewMemberId()");
		
		// productId 확인 로그
		log.info("memberId = " + memberId);
		
		// productId에 해당하는 댓글(리뷰) list을 전체 검색
		List<ReviewVO> reviewList = reviewService.getAllReviewMemberId(memberId);
		

		List<ProductVO> productList = new ArrayList<>(); // productList 초기화

		for (ReviewVO vo : reviewList) {
		    ProductVO product = productService.getProductById(vo.getProductId()); // 단일 ProductVO 반환
		    productList.add(product); // productList에 product 추가
		}
		
		log.info(productList);
		
		model.addAttribute("productList", productList);
		model.addAttribute("reviewList", reviewList);
	}// end readAllReview()

}
