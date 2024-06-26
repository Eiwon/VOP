package com.web.vop.persistence;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.ReviewVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ReviewMapper {
	
	 // 댓글(리뷰) 등록
	 int insertReview(ReviewVO reviewVO);
	 
	 // 댓글(리뷰) 전체 검색
	 List<ReviewVO> selectListByReview(int productId);
	 
	 // 댓글(리뷰) 전체 검색 페이징 처리
	 List<ReviewVO>selectListByReviewPaging(@Param("productId")int productId, @Param("pagination") Pagination pagination);
	 
	 int selectListByReviewCnt(int productId);
	 
	 // 댓글(리뷰) 회원ID로 전체 검색
	 List<ReviewVO> selectListByReviewMemberId(String memberId);
	 
	 // 댓글(리뷰) productId 그리고 memberId 통해 검색
	 ReviewVO selectByReview(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // 댓글(리뷰) 수정
	 int updateReview(ReviewVO reviewVO);
	 
	 // 댓글(리뷰) 삭제
	 int deleteReview(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // 댓글 좋아요 UP
	 int updateReviewLikeUp(int reviewId);
	 
	 // 댓글 좋아요 DOWN
	 int updateReviewLikeDown(int reviewId);
	 
	// 상품 리뷰(별) 총합 검색
	int selectReviewStar(int productId);
	
	// 상품의 리뷰 평균값 검색
	float selectReviewAgv(int productId);
}
