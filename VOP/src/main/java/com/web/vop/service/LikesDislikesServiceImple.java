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
		
		int insertRes = likesDislikesMapper.insertLikesDislikes(likesDislikesVO);
		int updateRes = likesDislikesMapper.updateLikesDislikes(likesDislikesVO);
		int deleteRes = likesDislikesMapper.deleteLikesDislikes(likesDislikesVO.getReviewId(), likesDislikesVO.getMemberId());
				
		return insertRes;
	}
	
	@Override
	public List<LikesDislikesVO> getAllReviewMemberId(int reviewId, String memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateLikesDislikes(int likeDislikeId, int likeDislikeType) {
		return 0;
	}

	@Override
	public int deleteLikesDislikes(int reviewId, String memberId) {
		return 0;
	}

	
	
}
