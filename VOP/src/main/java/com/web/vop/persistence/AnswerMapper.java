package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.AnswerVO;

@Mapper
public interface AnswerMapper {
	// ����(�亯) ���
	int insertAnswer(AnswerVO answerVO);
		 
	// ����(�亯) ��ü �˻�
	List<AnswerVO> selectListByProductId(int productId);
	
	// ����(�亯) ��ü �˻�
	List<AnswerVO> selectListByInquiryId(int inquiryId);
		 
	// ����(�亯) ����
	int updateAnswer(AnswerVO answerVO);
		 
	// ����(�亯) ����
	int deleteAnswer(@Param("inquiryId") int inquiryId, @Param("memberId") String memberId);
}
