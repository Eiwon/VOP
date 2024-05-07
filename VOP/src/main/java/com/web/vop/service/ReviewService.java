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
		 
	// ´ñ±Û(¸®ºä) ¼öÁ¤
	int updateReview(int reviewId, String reviewContent, float reviewStar);
		 
	// ´ñ±Û(¸®ºä) »èÁ¦
	int deleteReview(int reviewId, int productId);

	
} 
