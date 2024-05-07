package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.ReviewVO;

@Service
public interface ReviewService {
	// ���(����) ���
	int createReview(ReviewVO reviewVO);
		 
	// ���(����) ��ü �˻�
	List<ReviewVO> getAllReview(int productId);
		 
	// ���(����) ����
	int updateReview(int reviewId, String reviewContent, float reviewStar);
		 
	// ���(����) ����
	int deleteReview(int reviewId, int productId);

	
} 
