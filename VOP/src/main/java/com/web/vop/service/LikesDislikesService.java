package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.LikesDislikesVO;

public interface LikesDislikesService {
	
	// 좋아요 or 싫어요 등록
	int createLikesDislikes(LikesDislikesVO likesDislikesVO);
	
	// 좋아요 or 싫어요 전체 검색
	List<LikesDislikesVO> getAllReviewMemberId(int reviewId, String memberId);
		
	// 좋아요 or 싫어요 수정
	int updateLikesDislikes(LikesDislikesVO likesDislikesVO);
			 
	// 좋아요 or 싫어요 삭제
	int deleteLikesDislikes(int reviewId, String memberId);
	
}