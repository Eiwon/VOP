package com.web.vop.service;

import java.util.List;


import com.web.vop.domain.ReviewVO;
import com.web.vop.util.PageMaker;


public interface ReviewService {
	// ´ñ±Û(¸®ºä) µî·Ï
	int createReview(ReviewVO reviewVO);
		 
	// ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö
	List<ReviewVO> getAllReview(int productId);
	
	// ´ñ±Û(¸®ºä) È¸¿øID·Î ÀüÃ¼ °Ë»ö
	List<ReviewVO> getAllReviewPaging(int productId, PageMaker pageMaker);
	
	// ´ñ±Û(¸®ºä) È¸¿øID·Î ÀüÃ¼ °Ë»ö
	List<ReviewVO> getAllReviewMemberId(String memberId);
	
	// ´ñ±Û(¸®ºä) °Ë»ö
	ReviewVO selectByReview(int productId, String memberId);
		 
	// ´ñ±Û(¸®ºä) ¼öÁ¤
	int updateReview(int reviewId, String reviewContent, float reviewStar, int productId);
		 
	// ´ñ±Û(¸®ºä) »èÁ¦
	int deleteReview(int productId, String memberId);

} 
