package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ReviewVO;

@Mapper
public interface ReviewMapper {
	
	 // 댓글(리뷰) 등록
	 int insertReview(ReviewVO reviewVO);
	 
	 // 댓글(리뷰) 전체 검색
	 List<ReviewVO> selectListByReview(int productId);
	 
	 // 댓글(리뷰) 수정
	 int updateReview(ReviewVO reviewVO);
	 
	 // 댓글(리뷰) 삭제
	 int deleteReview(int reviewId);
	 
	 // 댓글(리뷰) 검색(댓글 내용에 입력한 단어가 포함 되어 있으면 검색 리스트 형태, 제작예정)
	 
}
