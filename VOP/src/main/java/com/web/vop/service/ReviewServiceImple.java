package com.web.vop.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.ReviewVO;
import com.web.vop.persistence.ReviewMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReviewServiceImple implements ReviewService{
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	// ´ñ±Û(¸®ºä) µî·Ï
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		int insertRes = reviewMapper.insertReview(reviewVO);
		log.info(insertRes + "Çà ´ñ±Û(¸®ºä) µî·Ï");
		return insertRes;
	}

	@Override
	public List<ReviewVO> getAllReview(int productId) {
		log.info("getAllReview()");
		List<ReviewVO> result = reviewMapper.selectListByReview(productId);
		return result;
	}

	@Override
	public int updateReview(int reviewId, String reviewContent, float reviewStar) {
		log.info("updateReview()");
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO¿¡ °¢ º¯°æ»çÇ× º¯¼öµé ÀúÀå
		reviewVO.setReviewId(reviewId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		int updateRes = reviewMapper.updateReview(reviewVO);
		log.info(updateRes + "Çà ´ñ±ÛÀÌ ¼öÁ¤µÇ¾ú½À´Ï´Ù.");
		return updateRes;
	}

	@Override
	public int deleteReview(int reviewId, int productId) {
		log.info("deleteReview()");
		int deleteRes = reviewMapper.deleteReview(reviewId);
		log.info(deleteRes + "Çà »èÁ¦");
		return deleteRes;
	}
	
	// ´ñ±Û ÃÑ °¹¼ö Áõ°¡
	@Override
	public int reviewNumUP(int productId) {
		log.info("reviewNumUP()");
		int res = reviewMapper.reviewNumUP(productId);
		return res;
	}
		
	// ´ñ±Û ÃÑ °¹¼ö °¨¼Ò
	@Override
	public int reviewNumDown(int productId) {
		log.info("reviewNumDown()");
		int res = reviewMapper.reviewNumDown(productId);
		return res;
	}

}
