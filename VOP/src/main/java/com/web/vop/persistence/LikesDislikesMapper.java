package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.LikesDislikesVO;

@Mapper
public interface LikesDislikesMapper {
	// ���ƿ� or �Ⱦ�� ���
	int insertLikesDislikes(LikesDislikesVO likesDislikesVO);
	
	// ���ƿ� or �Ⱦ�� ����
	List<LikesDislikesVO> selectBylikesDislikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
	
	// ���ƿ� or �Ⱦ�� ����
	int updateLikesDislikes(LikesDislikesVO likesDislikesVO);
		 
	// ���ƿ� or �Ⱦ�� ����
	int deleteLikesDislikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
}
