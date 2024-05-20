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
	
	// ���(����) ��ü �˻�
	ReviewVO selectByReview(int productId, String memberId);
		 
	// ���(����) ����
	int updateReview(int reviewId, String reviewContent, float reviewStar);
		 
	// ���(����) ����
	int deleteReview(int reviewId, int productId);

	// ��� �� ���� ����
	int reviewNumUP(int productId);
		
	// ��� �� ���� ����
	int reviewNumDown(int productId);
} 
