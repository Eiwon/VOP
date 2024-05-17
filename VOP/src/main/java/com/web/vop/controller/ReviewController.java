package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.service.ImageService;
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
	
	// ´ñ±Û(¸®ºä) µî·Ï GET
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
	
	
	// ´ñ±Û(¸®ºä) ¼öÁ¤ GET
	@GetMapping("/modify")
	public void updateReviewGET(Model model, Integer productId, Integer imgId, String memberId) {
		log.info("updateReviewGET()");
		
		log.info("productId : " + productId);
		log.info("imgId : " + imgId);
		
		int Id = productId;
		
		ReviewVO reviewVO = reviewService.selectByReview(Id, memberId);
		int reviewId = reviewVO.getReviewId();

		ImageVO imageVO = imageService.getImageById(imgId);
		
		String imgRealName = imageVO.getImgRealName();
		String imgExtension = imageVO.getImgExtension();
		
		log.info("imageVO : " + imageVO);
		
		log.info("imgRealName : " + imgRealName);
		log.info("imgExtension : " + imgExtension);
		
		model.addAttribute("imgRealName", imgRealName);
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("imgId", imgId);
		model.addAttribute("imgExtension", imgExtension);
	} // end loginGET
	
}
