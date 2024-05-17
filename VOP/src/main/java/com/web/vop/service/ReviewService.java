package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ReviewVO;

@Service
public interface ReviewService {
	// ´ñ±Û(¸®ºä) µî·Ï
	int createReview(ReviewVO reviewVO);
		 
	// ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö
	List<ReviewVO> getAllReview(int productId);
	
	// ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö
	ReviewVO selectByReview(int productId, String memberId);
		 
	// ´ñ±Û(¸®ºä) ¼öÁ¤
	int updateReview(int reviewId, String reviewContent, float reviewStar);
		 
	// ´ñ±Û(¸®ºä) »èÁ¦
	int deleteReview(int reviewId, int productId);

	// ´ñ±Û ÃÑ °¹¼ö Áõ°¡
	int reviewNumUP(int productId);
		
	// ´ñ±Û ÃÑ °¹¼ö °¨¼Ò
	int reviewNumDown(int productId);
} 
