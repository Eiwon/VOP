package com.web.vop.persistence;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.ReviewVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ReviewMapper {
	
	 // ���(����) ���
	 int insertReview(ReviewVO reviewVO);
	 
	 // ���(����) ��ü �˻�
	 List<ReviewVO> selectListByReview(int productId);
	 
	 // ���(����) ��ü �˻� ����¡ ó��
	 List<ReviewVO>selectListByReviewPaging(@Param("productId")int productId, @Param("pagination") Pagination pagination);
	 
	 int selectListByReviewCnt(int productId);
	 
	 // ���(����) ȸ��ID�� ��ü �˻�
	 List<ReviewVO> selectListByReviewMemberId(String memberId);
	 
	 // ���(����) productId �׸��� memberId ���� �˻�
	 ReviewVO selectByReview(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // ���(����) ����
	 int updateReview(ReviewVO reviewVO);
	 
	 // ���(����) ����
	 int deleteReview(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // ��� ���ƿ� UP
	 int updateReviewLikeUp(int reviewId);
	 
	 // ��� ���ƿ� DOWN
	 int updateReviewLikeDown(int reviewId);
	 
	// ��ǰ ����(��) ���� �˻�
	int selectReviewStar(int productId);
	
	// ��ǰ�� ���� ��հ� �˻�
	float selectReviewAgv(int productId);
}
