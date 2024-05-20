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
	
	// 댓글(리뷰) 등록
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		int insertRes = reviewMapper.insertReview(reviewVO);
		log.info(insertRes + "행 댓글(리뷰) 등록");
		return insertRes;
	}
	
	// 댓글(리뷰) 전체 검색
	@Override
	public List<ReviewVO> getAllReview(int productId) {
		log.info("getAllReview()");
		List<ReviewVO> result = reviewMapper.selectListByReview(productId);
		return result;
	}
	// 댓글(리뷰) memberId로 전체 검색
	@Override
	public List<ReviewVO> getAllReviewMemberId(String memberId) {
		log.info("getAllReviewMemberId()");
		List<ReviewVO> result = reviewMapper.selectListByReviewMemberId(memberId);
		return result;
	}
	
	// 댓글(리뷰) productId 그리고 memberId통해 검색
	@Override
	public ReviewVO selectByReview(int productId, String memberId) {
		log.info("selectByReview()");
		log.info("productId : " + productId);
		log.info("memberId : " + memberId);
		ReviewVO result = reviewMapper.selectByReview(productId, memberId);
		return result;
	}
	
	// 댓글(리뷰) 수정
	@Override
	public int updateReview(int reviewId, String reviewContent, float reviewStar) {
		log.info("updateReview()");
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO에 각 변경사항 변수들 저장
		reviewVO.setReviewId(reviewId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		int updateRes = reviewMapper.updateReview(reviewVO);
		log.info(updateRes + "행 댓글이 수정되었습니다.");
		return updateRes;
	}

	// 댓글(리뷰) 삭제
	@Override
	public int deleteReview(int reviewId, int productId) {
		log.info("deleteReview()");
		int deleteRes = reviewMapper.deleteReview(reviewId);
		log.info(deleteRes + "행 삭제");
		return deleteRes;
	}

	


	
	

	

}
