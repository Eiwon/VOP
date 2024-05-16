package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.AnswerVO;

@Mapper
public interface AnswerMapper {
	// ����(�亯) ���
	int insertAnswer(AnswerVO answerVO);
		 
//	// ����(�亯) ��ü �˻�
//	List<AnswerVO> selectListByAnswer(int productId);
		 
	// ����(�亯) ����
	int updateAnswer(AnswerVO answerVO);
		 
	// ����(�亯) ����
	int deleteAnswer(int productId, String memberId);
}
