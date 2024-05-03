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
	 
	 // ���(����) ����
	 int updateReview(ReviewVO reviewVO);
	 
	 // ���(����) ����
	 int deleteReview(int reviewId);
	 
	 // ���(����) �˻�(��� ���뿡 �Է��� �ܾ ���� �Ǿ� ������ �˻� ����Ʈ ����, ���ۿ���)
	 
}
