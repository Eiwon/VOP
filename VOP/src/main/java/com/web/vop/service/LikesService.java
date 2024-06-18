package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.LikesVO;

public interface LikesService {
	
	// 좋아요 or 싫어요 등록
	int createLikes(LikesVO likesVO);
	
	// 좋아요 or 싫어요 전체 검색
	List<LikesVO> getAllLikes(int reviewId, String memberId);
		
	// 좋아요 or 싫어요 수정
	int updateLikes(LikesVO likesVO);
			 
	// 좋아요 or 싫어요 삭제
	int deleteLikes(int reviewId, String memberId);
	
}