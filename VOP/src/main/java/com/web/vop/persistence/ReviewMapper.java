package com.web.vop.persistence;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.ReviewVO;

@Mapper
public interface ReviewMapper {
	
	 // ���(����) ���
	 int insertReview(ReviewVO reviewVO);
	 
	 // ���(����) ��ü �˻�
	 List<ReviewVO> selectListByReview(int productId);
	 
	 // ���(����) ȸ��ID�� ��ü �˻�
	 List<ReviewVO> selectListByReviewMemberId(String memberId);
	 
	 // ���(����) productId �׸��� memberId ���� �˻�
	 ReviewVO selectByReview(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // ���(����) ����
	 int updateReview(ReviewVO reviewVO);
	 
	 // ���(����) ����
	 int deleteReview(int reviewId);
	 
	 
	
	 
	 // ���(����) �˻�(��� ���뿡 �Է��� �ܾ ���� �Ǿ� ������ �˻� ����Ʈ ����, ���ۿ���)
	 
}
