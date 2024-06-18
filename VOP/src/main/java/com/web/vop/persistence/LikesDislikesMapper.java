package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.LikesDislikesVO;

@Mapper
public interface LikesDislikesMapper {
	// 좋아요 or 싫어요 등록
	int insertLikesDislikes(LikesDislikesVO likesDislikesVO);
	
	// 좋아요 or 싫어요 수정
	List<LikesDislikesVO> selectBylikesDislikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
	
	// 좋아요 or 싫어요 수정
	int updateLikesDislikes(LikesDislikesVO likesDislikesVO);
		 
	// 좋아요 or 싫어요 삭제
	int deleteLikesDislikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
}
