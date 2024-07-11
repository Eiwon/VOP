package com.web.vop.persistence;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.ReviewVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ReviewMapper {
	
	 // ´ñ±Û(¸®ºä) µî·Ï
	 int insertReview(ReviewVO reviewVO);
	 
	 // ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö
	 List<ReviewVO> selectListByReview(int productId);
	 
	 // ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö ÆäÀÌÂ¡ Ã³¸®
	 List<ReviewVO>selectListByReviewPaging(@Param("productId")int productId, @Param("pagination") Pagination pagination);
	 
	 int selectListByReviewCnt(int productId);
	 
	 // ´ñ±Û(¸®ºä) È¸¿øID·Î ÀüÃ¼ °Ë»ö
	 List<ReviewVO> selectListByReviewMemberId(String memberId);
	 
	 // ´ñ±Û(¸®ºä) productId ±×¸®°í memberId ÅëÇØ °Ë»ö
	 ReviewVO selectByReview(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // ´ñ±Û(¸®ºä) ¼öÁ¤
	 int updateReview(ReviewVO reviewVO);
	 
	 // ´ñ±Û(¸®ºä) »èÁ¦
	 int deleteReview(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // ´ñ±Û ÁÁ¾Æ¿ä UP
	 int updateReviewLikeUp(int reviewId);
	 
	 // ´ñ±Û ÁÁ¾Æ¿ä DOWN
	 int updateReviewLikeDown(int reviewId);
	 
	// ´ñ±Û ½È¾î¿ä UP
	int updateReviewDislikeUp(int reviewId);
		 
	// ´ñ±Û ½È¾î¿ä DOWN
	int updateReviewDislikeDown(int reviewId);
	 
	// ´ñ±Û ÁÁ¾Æ¿ä UP ½È¾î¿ä DOWN
	int updateReviewLikeUpDown(int reviewId);
		 
	// ´ñ±Û ÁÁ¾Æ¿ä DOWN ½È¾î¿äUP
	int updateReviewLikeDownUp(int reviewId);
	 
	// »óÇ° ¸®ºä(º°) ÃÑÇÕ °Ë»ö
	int selectReviewStar(int productId);
	
	// »óÇ°ÀÇ ¸®ºä Æò±Õ°ª °Ë»ö
	float selectReviewAgv(int productId);
}
