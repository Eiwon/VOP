package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.ReviewVO;

@Mapper
public interface ReviewMapper {
	
	 // ���(����) ���
	 int insertReview(ReviewVO reviewVO);
	 
	 // ���(����) ��ü �˻�
	 List<ReviewVO> selectListByReview(int productId);
	 
	 // ���(����) productId �׸��� memberId ���� �˻�
	 ReviewVO selectByReview(int productId, String memberId);
	 
	 // ���(����) ����
	 int updateReview(ReviewVO reviewVO);
	 
	 // ���(����) ����
	 int deleteReview(int reviewId);
	 
	// ��� �� ���� ����
	int reviewNumUP(int productId);
		
	// ��� �� ���� ����
	int reviewNumDown(int productId);
	 
	 // ���(����) �˻�(��� ���뿡 �Է��� �ܾ ���� �Ǿ� ������ �˻� ����Ʈ ����, ���ۿ���)
	 
}
