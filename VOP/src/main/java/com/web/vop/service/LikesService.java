package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.LikesVO;

public interface LikesService {
	
	// ���ƿ� or �Ⱦ�� ���
	int createLikes(LikesVO likesVO);
	
	List<LikesVO> getAllLikesPaging(@Param("memberId") String memberId, @Param("reviewIds") List<String> reviewIds);
	
	// ���ƿ� or �Ⱦ�� ��ü �˻�
	List<LikesVO> getAllLikes(int reviewId, String memberId);
		
	// ���ƿ� or �Ⱦ�� ����
	int updateLikes(LikesVO likesVO);
			 
	// ���ƿ� or �Ⱦ�� ����
	int deleteLikes(int reviewId, String memberId);
	
}