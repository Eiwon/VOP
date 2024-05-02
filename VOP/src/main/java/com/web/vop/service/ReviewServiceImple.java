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
	
	@Autowired
	private ProductMapper productMapper;
	
	// ���(����) ���
	@Override
	public int createReview(ReviewVO reviewVO) {
		log.info("createReview()");
		int insertRes = reviewMapper.insertReview(reviewVO);
		log.info(insertRes + "�� ���(����) ���");
		// ��ǰ�� ��� ī���� ���� �ؾ���
//		 int updateresult = boardMapper.updateReplyCount(replyVO.getBoardId(), 1);
//	     log.info(updateresult + "�� �Խ��� ����");
		return insertRes;
	}

	@Override
	public List<ReviewVO> getAllReview(int productId) {
		log.info("getAllReview()");
		List<ReviewVO> result = reviewMapper.selectListByReview(productId);
		return result;
	}

	@Override
	public int updateReview(int reviewId, String reviewContent, float reviewStar, int imgId) {
		log.info("updateReview()");
		ReviewVO reviewVO = new ReviewVO();
		// reviewVO�� �� ������� ������ ����
		reviewVO.setReviewId(reviewId);
		reviewVO.setReviewContent(reviewContent);
		reviewVO.setReviewStar(reviewStar);
		reviewVO.setImgId(imgId);
		int updateRes = reviewMapper.updateReview(reviewVO);
		log.info(updateRes + "�� ����� �����Ǿ����ϴ�.");
		return updateRes;
	}

	@Override
	public int deleteReview(int reviewId, int productId) {
		log.info("deleteReview()");
		int deleteRes = reviewMapper.deleteReview(reviewId);
		log.info(deleteRes + "�� ����");
		// ��ǰ�� ��� ī���� ���� �ؾ���
		// ����� �ڵ�
		// int updateResult = boardMapper.updateReplyCount(boardId, -1);
	    // log.info(updateResult + "�� ��� ����");
		return deleteRes;
	}

}
