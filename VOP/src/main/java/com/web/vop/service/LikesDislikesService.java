package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.LikesDislikesVO;

public interface LikesDislikesService {
	
	// ���ƿ� or �Ⱦ�� ���
	int createLikesDislikes(LikesDislikesVO likesDislikesVO);
	
	// ���ƿ� or �Ⱦ�� ��ü �˻�
	List<LikesDislikesVO> getAllReviewMemberId(int reviewId, String memberId);
		
	// ���ƿ� or �Ⱦ�� ����
	int updateLikesDislikes(LikesDislikesVO likesDislikesVO);
			 
	// ���ƿ� or �Ⱦ�� ����
	int deleteLikesDislikes(int reviewId, String memberId);
	
}