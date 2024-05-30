package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.AnswerVO;
import com.web.vop.persistence.AnswerMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class AnswerServiceImple implements AnswerService{
	
	@Autowired
	private AnswerMapper answerMapper;
	
	
	// 댓댓글(답변) 등록
	@Override
	public int createAnswer(AnswerVO answerVO) {
		log.info("createAnswer()");
		log.info("answerVO : " + answerVO);
		
		int inquiryId = answerVO.getInquiryId();
		
		String memberId = answerVO.getMemberId();
		
		log.info("InquiryId : " + inquiryId);
		log.info("memberId : " + memberId);
		
		AnswerVO vo = answerMapper.selectListByInquiryId(inquiryId);
		
		int insertRes = 0;
		if(vo == null) {
			insertRes = answerMapper.insertAnswer(answerVO);
			log.info(insertRes + "행 댓댓글(답변) 등록");
		}
		
		return insertRes;
	}
	
	// 댓댓글(답변) 전체 검색
	@Override
	public List<AnswerVO> getAllAnswer(int productId) {
		log.info("getAllAnswer()");
		List<AnswerVO> result = answerMapper.selectListByProductId(productId);
		return result;
	}
	
	// 댓댓글(답변) 전체 검색(사용안함)
	@Override
	public AnswerVO getAllAnswerInquiryId(int inquiryId) {
		log.info("getAllAnswer()");
		AnswerVO result = answerMapper.selectListByInquiryId(inquiryId);
		return result;
	}
	
	// 댓댓글(답변) 수정
	@Override
	public int updateAnswer(int inquiryId, String memberId, String answerContent) {
		log.info("updateAnswer()");
		
		AnswerVO vo = answerMapper.selectListByInquiryId(inquiryId);
		
		int updateRes = 0;
		if(vo != null) {
			AnswerVO answerVO = new AnswerVO();
			// reviewVO에 각 변경사항 변수들 저장
			answerVO.setInquiryId(inquiryId);
			answerVO.setMemberId(memberId);
			answerVO.setAnswerContent(answerContent);
			updateRes = answerMapper.updateAnswer(answerVO);
			log.info(updateRes + "행 댓댓글(답변)이 수정되었습니다.");
		}
		
		return updateRes;
	}

	// 댓댓글(답변) 삭제
	@Override
	public int deleteAnswer(int inquiryId, String memberId) {
		log.info("deleteAnswer()");
		int deleteRes = answerMapper.deleteAnswer(inquiryId, memberId);
		log.info(deleteRes + "행 삭제");
		return deleteRes;
	}

}
