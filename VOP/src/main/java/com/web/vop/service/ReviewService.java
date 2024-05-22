package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.ReviewVO;


public interface ReviewService {
	// ���(����) ���
	int createReview(ReviewVO reviewVO);
		 
	// ���(����) ��ü �˻�
	List<ReviewVO> getAllReview(int productId);
	
	// ���(����) ȸ��ID�� ��ü �˻�
	List<ReviewVO> getAllReviewMemberId(String memberId);
	
	// ���(����) �˻�
	ReviewVO selectByReview(int productId, String memberId);
		 
	// ���(����) ����
	int updateReview(int reviewId, String reviewContent, float reviewStar);
		 
	// ���(����) ����
	int deleteReview(int productId, String memberId);

	
} 
