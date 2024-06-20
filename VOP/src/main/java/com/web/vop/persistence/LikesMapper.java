package com.web.vop.persistence;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.LikesVO;

@Mapper
public interface LikesMapper {
	// ���ƿ� or �Ⱦ�� ���
	int insertLikes(LikesVO likesVO);
	
	List<LikesVO> selectByLikesPaging(@Param("memberId") String memberId, @Param("reviewIds") List<String> reviewIds);
	
	// ���ƿ� or �Ⱦ�� �˻�
	List<LikesVO> selectByLikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
	
	// ���ƿ� or �Ⱦ�� ����
	int updateLikes(LikesVO likesVO);
		 
	// ���ƿ� or �Ⱦ�� ����
	int deleteLikes(@Param("reviewId")int reviewId, @Param("memberId")String memberId);
}
