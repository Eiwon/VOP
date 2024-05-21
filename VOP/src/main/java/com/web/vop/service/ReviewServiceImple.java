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
	
	// ���(����) ���
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		int insertRes = reviewMapper.insertReview(reviewVO);
		log.info(insertRes + "�� ���(����) ���");
		return insertRes;
	}
	
	// ���(����) ��ü �˻�
	@Override
	public List<ReviewVO> getAllReview(int productId) {
		log.info("getAllReview()");
		List<ReviewVO> result = reviewMapper.selectListByReview(productId);
		return result;
	}
	// ���(����) memberId�� ��ü �˻�
	@Override
	public List<ReviewVO> getAllReviewMemberId(String memberId) {
		log.info("getAllReviewMemberId()");
		List<ReviewVO> result = reviewMapper.selectListByReviewMemberId(memberId);
		return result;
	}
	
	// ���(����) productId �׸��� memberId���� �˻�
	@Override
	public ReviewVO selectByReview(int productId, String memberId) {
		log.info("selectByReview()");
		log.info("productId : " + productId);
		log.info("memberId : " + memberId);
		ReviewVO result = reviewMapper.selectByReview(productId, memberId);
		return result;
	}
	
	// ���(����) ����
	@Override
	public int updateReview(int reviewId, String reviewContent, float reviewStar) {
		log.info("updateReview()");
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO�� �� ������� ������ ����
		reviewVO.setReviewId(reviewId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		int updateRes = reviewMapper.updateReview(reviewVO);
		log.info(updateRes + "�� ����� �����Ǿ����ϴ�.");
		return updateRes;
	}

	// ���(����) ����
	@Override
	public int deleteReview(int productId, String memberId) {
		log.info("deleteReview()");
		int deleteRes = reviewMapper.deleteReview(productId, memberId);
		log.info(deleteRes + "�� ����");
		return deleteRes;
	}

	


	
	

	

}
