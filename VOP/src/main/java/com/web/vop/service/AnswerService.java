package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.AnswerVO;

public interface AnswerService {
	
	// ����(�亯) ���
	int createAnswer(AnswerVO answerVO);
				 
//	// ����(�亯) ��ü �˻�
//	List<AnswerVO> getAllAnswer(int productId);
				 
	// ����(�亯) ����
	int updateAnswer(int productId, String memberId, String answerContent);
				 
	// ����(�亯) ����
	int deleteAnswer(int productId, String memberId);
}
