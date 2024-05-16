package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.AnswerVO;

public interface AnswerService {
	
	// ´ñ´ñ±Û(´äº¯) µî·Ï
	int createAnswer(AnswerVO answerVO);
				 
//	// ´ñ´ñ±Û(´äº¯) ÀüÃ¼ °Ë»ö
//	List<AnswerVO> getAllAnswer(int productId);
				 
	// ´ñ´ñ±Û(´äº¯) ¼öÁ¤
	int updateAnswer(int productId, String memberId, String answerContent);
				 
	// ´ñ´ñ±Û(´äº¯) »èÁ¦
	int deleteAnswer(int productId, String memberId);
}
