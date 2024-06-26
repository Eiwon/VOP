package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ReviewVO;
import com.web.vop.util.PageMaker;


public interface ReviewService {
	// ���(����) ���
	int createReview(ReviewVO reviewVO);
		 
	// ���(����) ��ü �˻�
	List<ReviewVO> getAllReview(int productId);
	
	// ���(����) ȸ��ID�� ��ü �˻�
	List<ReviewVO> getAllReviewPaging(int productId, PageMaker pageMaker);
	
	// ���(����) ȸ��ID�� ��ü �˻�
	List<ReviewVO> getAllReviewMemberId(String memberId);
	
	// ���(����) �˻�
	ReviewVO selectByReview(int productId, String memberId);
		 
	// ���(����) ����
	int updateReview(String memberId, String reviewContent, float reviewStar, int productId);
		 
	// ���(����) ����
	int deleteReview(int productId, String memberId);

	// ����� �ۼ��� ��ǰ ���� �˻�
	ProductPreviewDTO getProductPreview(int productId);
	
} 
