package com.web.vop.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.LikesVO;
import com.web.vop.persistence.LikesMapper;
import com.web.vop.persistence.ReviewMapper;

import lombok.extern.log4j.Log4j;


@Service
@Log4j
public class LikesServiceImple implements LikesService{
	
	@Autowired
	private LikesMapper likesMapper;
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	// 좋아요 or 싫어요 등록
	@Transactional(value = "transactionManager")
	@Override
	public int createLikes(LikesVO likesVO) {
		log.info("createLikesDislikes()");
		log.info("likesVO : " + likesVO);
		
		int reviewId = likesVO.getReviewId();
		log.info("reviewId : " + reviewId);
		
		// 좋아요 or 싫어요 등록
		int insertRes = likesMapper.insertLikes(likesVO);
		
		// 좋아요일 경우 리뷰 좋아요 갯수 증가
		if(likesVO.getLikesType() == 1) {
			int insertReviewLikes = reviewMapper.updateReviewLikeUp(reviewId);
			log.info(insertRes + "행 좋아요 등록 " + insertReviewLikes + "행 좋아요 갯수 UP");
		}
		log.info(insertRes + "행 좋아요 등록 ");
		return insertRes;
	}
	
	
	@Override
	public List<LikesVO> getAllLikes(int reviewId, String memberId) {
		return null;
	}
	
	// 좋아요 or 싫어요 수정
	@Transactional(value = "transactionManager")
	@Override
	public int updateLikes(LikesVO likesVO) {
		log.info("updateLikesDislikes()");
		int reviewId = likesVO.getReviewId();
		int likesType = likesVO.getLikesType();
		int updateRes = likesMapper.updateLikes(likesVO);
		if(likesType == 1) {
			int insertReviewLikes = reviewMapper.updateReviewLikeUp(reviewId);
			log.info(updateRes + "행 수정" + insertReviewLikes + "행 좋아요 갯수 UP");
		}else if(likesType == 0) {
			int insertReviewLikes = reviewMapper.updateReviewLikeDown(reviewId);
			log.info(updateRes + "행 수정" + insertReviewLikes + "행 좋아요 갯수 DOWN");
		}
		
		return updateRes;
	}
	
	// 좋아요 or 싫어요 삭제
	@Transactional(value = "transactionManager")
	@Override
	public int deleteLikes(int reviewId, String memberId, int likesType) {
		log.info("deleteLikesDislikes()");
		int deleteRes = likesMapper.deleteLikes(reviewId, memberId);
		if(likesType == 1) {
			int insertReviewLikes = reviewMapper.updateReviewLikeDown(reviewId);
			log.info(deleteRes + "행 삭제" + insertReviewLikes + "행 좋아요 갯수 DOWN");
		} else {
			log.info(deleteRes + "행 삭제");
		}
		
		return deleteRes;
	}
	
	// 좋아요 or 싫어요 검색 
	@Override
	public List<LikesVO> getAllLikesPaging(int productId, String memberId) {
		log.info("getAllLikesPaging()");
		List<LikesVO> list = likesMapper.selectByLikesPaging(productId, memberId);
		log.info("list : " + list);
		return list;
	}
	
}
