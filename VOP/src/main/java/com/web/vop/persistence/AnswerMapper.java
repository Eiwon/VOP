package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.AnswerVO;

@Mapper
public interface AnswerMapper {
	// ´ñ´ñ±Û(´äº¯) µî·Ï
	int insertAnswer(AnswerVO answerVO);
		 
	// ´ñ´ñ±Û(´äº¯) ÀüÃ¼ °Ë»ö
	List<AnswerVO> selectListByProductId(int productId);
	
	// ´ñ´ñ±Û(´äº¯) ÀüÃ¼ °Ë»ö
	List<AnswerVO> selectListByInquiryId(int inquiryId);
		 
	// ´ñ´ñ±Û(´äº¯) ¼öÁ¤
	int updateAnswer(AnswerVO answerVO);
		 
	// ´ñ´ñ±Û(´äº¯) »èÁ¦
	int deleteAnswer(@Param("inquiryId") int inquiryId, @Param("memberId") String memberId);
}
