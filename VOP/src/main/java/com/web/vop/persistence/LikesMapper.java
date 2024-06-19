package com.web.vop.persistence;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.LikesVO;

@Mapper
public interface LikesMapper {
	// 좋아요 or 싫어요 등록
	int insertLikes(LikesVO likesVO);
	
	List<LikesVO> selectByLikesPaging(@Param("memberId") String memberId, @Param("reviewIds") List<String> reviewIds);
	
	// 좋아요 or 싫어요 검색
	List<LikesVO> selectByLikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
	
	// 좋아요 or 싫어요 수정
	int updateLikes(LikesVO likesVO);
		 
	// 좋아요 or 싫어요 삭제
	int deleteLikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
}
