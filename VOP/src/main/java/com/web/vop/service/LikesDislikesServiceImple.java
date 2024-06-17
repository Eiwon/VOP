package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.LikesDislikesVO;
import com.web.vop.persistence.LikesDislikesMapper;

import lombok.extern.log4j.Log4j;


@Service
@Log4j
public class LikesDislikesServiceImple implements LikesDislikesService{
	
	@Autowired
	private LikesDislikesMapper likesDislikesMapper;
	
	@Override
	public int createLikesDislikes(LikesDislikesVO likesDislikesVO) {
		log.info("createLikesDislikes()");
		int insertRes = likesDislikesMapper.insertLikesDislikes(likesDislikesVO);
		log.info(insertRes + "행 등록");
				
		return insertRes;
	}
	
	@Override
	public List<LikesDislikesVO> getAllReviewMemberId(int reviewId, String memberId) {
		return null;
	}

	@Override
	public int updateLikesDislikes(LikesDislikesVO likesDislikesVO) {
		log.info("updateLikesDislikes()");
		int updateRes = likesDislikesMapper.updateLikesDislikes(likesDislikesVO);
		log.info(updateRes + "행 수정");
		return updateRes;
	}

	@Override
	public int deleteLikesDislikes(int reviewId, String memberId) {
		log.info("deleteLikesDislikes()");
		int deleteRes = likesDislikesMapper.deleteLikesDislikes(reviewId, memberId);
		log.info(deleteRes + "행 삭제");
		return deleteRes;
	}
}
