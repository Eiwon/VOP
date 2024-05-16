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
	
	
	// ����(�亯) ���
	@Override
	public int createAnswer(AnswerVO answerVO) {
		log.info("createAnswer()");
		int insertRes = answerMapper.insertAnswer(answerVO);
		log.info(insertRes + "�� ����(�亯) ���");
		return insertRes;
	}
	
	// ����(�亯) ��ü �˻�
//	@Override
//	public List<AnswerVO> getAllAnswer(int productId) {
//		log.info("getAllAnswer()");
//		List<AnswerVO> result = answerMapper.selectListByAnswer(productId);
//		return result;
//	}
	
	// ����(�亯) ����
	@Override
	public int updateAnswer(int productId, String memberId, String answerContent) {
		log.info("updateAnswer()");
		AnswerVO answerVO = new AnswerVO();
		// reviewVO�� �� ������� ������ ����
		answerVO.setProductId(productId);
		answerVO.setMemberId(memberId);
		answerVO.setAnswerContent(answerContent);
		int updateRes = answerMapper.updateAnswer(answerVO);
		log.info(updateRes + "�� ����(�亯)�� �����Ǿ����ϴ�.");
		return updateRes;
	}

	// ����(�亯) ����
	@Override
	public int deleteAnswer(int productId, String memberId) {
		log.info("deleteAnswer()");
		int deleteRes = answerMapper.deleteAnswer(productId, memberId);
		log.info(deleteRes + "�� ����");
		return deleteRes;
	}

}