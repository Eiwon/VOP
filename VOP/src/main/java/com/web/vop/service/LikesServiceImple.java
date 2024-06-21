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
	
	// ���ƿ� or �Ⱦ�� ���
	@Transactional(value = "transactionManager")
	@Override
	public int createLikes(LikesVO likesVO) {
		log.info("createLikesDislikes()");
		log.info("likesVO : " + likesVO);
		
		int reviewId = likesVO.getReviewId();
		log.info("reviewId : " + reviewId);
		
		// ���ƿ� or �Ⱦ�� ���
		int insertRes = likesMapper.insertLikes(likesVO);
		
		// ���ƿ��� ��� ���� ���ƿ� ���� ����
		if(likesVO.getLikesType() == 1) {
			int insertReviewLikes = reviewMapper.updateReviewLikeUp(reviewId);
			log.info(insertRes + "�� ���ƿ� ��� " + insertReviewLikes + "�� ���ƿ� ���� UP");
		}
		log.info(insertRes + "�� ���ƿ� ��� ");
		return insertRes;
	}
	
	
	@Override
	public List<LikesVO> getAllLikes(int reviewId, String memberId) {
		return null;
	}
	
	// ���ƿ� or �Ⱦ�� ����
	@Transactional(value = "transactionManager")
	@Override
	public int updateLikes(LikesVO likesVO) {
		log.info("updateLikesDislikes()");
		int reviewId = likesVO.getReviewId();
		int likesType = likesVO.getLikesType();
		int updateRes = likesMapper.updateLikes(likesVO);
		if(likesType == 1) {
			int insertReviewLikes = reviewMapper.updateReviewLikeUp(reviewId);
			log.info(updateRes + "�� ����" + insertReviewLikes + "�� ���ƿ� ���� UP");
		}else if(likesType == 0) {
			int insertReviewLikes = reviewMapper.updateReviewLikeDown(reviewId);
			log.info(updateRes + "�� ����" + insertReviewLikes + "�� ���ƿ� ���� DOWN");
		}
		
		return updateRes;
	}
	
	// ���ƿ� or �Ⱦ�� ����
	@Transactional(value = "transactionManager")
	@Override
	public int deleteLikes(int reviewId, String memberId, int likesType) {
		log.info("deleteLikesDislikes()");
		int deleteRes = likesMapper.deleteLikes(reviewId, memberId);
		if(likesType == 1) {
			int insertReviewLikes = reviewMapper.updateReviewLikeDown(reviewId);
			log.info(deleteRes + "�� ����" + insertReviewLikes + "�� ���ƿ� ���� DOWN");
		} else {
			log.info(deleteRes + "�� ����");
		}
		
		return deleteRes;
	}
	
	// ���ƿ� or �Ⱦ�� �˻� 
	@Override
	public List<LikesVO> getAllLikesPaging(int productId, String memberId) {
		log.info("getAllLikesPaging()");
		List<LikesVO> list = likesMapper.selectByLikesPaging(productId, memberId);
		log.info("list : " + list);
		return list;
	}
	
}
