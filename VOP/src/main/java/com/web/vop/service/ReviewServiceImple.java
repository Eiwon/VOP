package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.ReviewVO;
import com.web.vop.persistence.ProductMapper;
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
		// 상품에 댓글 카운터 제작 해야함
//		 int updateresult = boardMapper.updateReplyCount(replyVO.getBoardId(), 1);
//	     log.info(updateresult + "행 게시판 수정");
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
		// reviewVO에 각 변경사항 변수들 저장
		reviewVO.setReviewId(reviewId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		int updateRes = reviewMapper.updateReview(reviewVO);
		log.info(updateRes + "행 댓글이 수정되었습니다.");
		return updateRes;
	}

	@Override
	public int deleteReview(int reviewId, int productId) {
		log.info("deleteReview()");
		int deleteRes = reviewMapper.deleteReview(reviewId);
		log.info(deleteRes + "행 삭제");
		// 상품에 댓글 카운터 제작 해야함
		// 강사님 코드
		// int updateResult = boardMapper.updateReplyCount(boardId, -1);
	    // log.info(updateResult + "행 댓글 삭제");
		return deleteRes;
	}
	
	// 댓글 총 갯수 증가
	@Override
	public int reviewNumUP(int productId) {
		log.info("reviewNumUP()");
		int res = reviewMapper.reviewNumUP(productId);
		return res;
	}
		
	// 댓글 총 갯수 감소
	@Override
	public int reviewNumDown(int productId) {
		log.info("reviewNumDown()");
		int res = reviewMapper.reviewNumDown(productId);
		return res;
	}

}
