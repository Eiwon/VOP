package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.InquiryVO;
import com.web.vop.persistence.AnswerMapper;
import com.web.vop.persistence.InquiryMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class InquiryServiceImple implements InquiryService{
	
	@Autowired
	private InquiryMapper inquiryMapper;
	
	@Autowired
	private AnswerMapper answerMapper;
	
	// ���(����) ���
	@Override
	public int createInquiry(InquiryVO inquiryVO) {
		log.info("createInquiry()");
		int insertRes = inquiryMapper.insertInquiry(inquiryVO);
		return insertRes;
	}
	
	// ���(����) ��ü �˻�
	@Override
	public List<InquiryVO> getAllInquiry(int productId) {
		log.info("getAllInquiry()");
		List<InquiryVO> result = inquiryMapper.selectListByInquiry(productId);
		return result;
	}
	
	// ���(����) �˻�
	@Override
	public InquiryVO selectByInquiry(int productId, String memberId) {
		log.info("selectByInquiry()");
		InquiryVO inquiryVO = inquiryMapper.selectByInquiry(productId, memberId);
		return inquiryVO;
	}
	
	// ���(����) ����
	@Override
	public int updateInquiry(int productId, String memberId, String inquiryContent) {
		log.info("updateInquiry()");
		log.info("�α�1");
		
		InquiryVO inquiryVO = new InquiryVO();
		log.info("inquiryVO : " + inquiryVO);
		inquiryVO.setProductId(productId);
		inquiryVO.setMemberId(memberId);
		inquiryVO.setInquiryContent(inquiryContent);
		int updateRes = inquiryMapper.updateInquiry(inquiryVO);
		log.info(updateRes + "�� ����� �����Ǿ����ϴ�.");
		return updateRes;
	}
	
	// ���(����) ����
	@Override
	public int deleteInquiry(int productId, String memberId, int inquiryId) {
		log.info("deleteInquiry()");
		int deleteRes = inquiryMapper.deleteInquiry(productId, memberId);
		
		if(deleteRes == 1) {
			int result = answerMapper.deleteAnswer(inquiryId, memberId);
			log.info("result : " + result);
		}
		return deleteRes;
	}
	
	

}
