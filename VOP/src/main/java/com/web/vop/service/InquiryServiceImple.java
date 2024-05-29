package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(value = "transactionManager")// �亯(����)�� ���� ���� �ϰ� ����(���)�����͸� �����ؾ��� �ܷ�Ű ��Ģ�� �´´�.
	@Override
	public int deleteInquiry(int productId, String memberId) {
		log.info("deleteInquiry()");
		log.info("productId : " + productId);
		log.info("memberId : " + memberId);
		
		int deleteRes = inquiryMapper.deleteInquiry(productId, memberId);
	
		log.info(deleteRes + "�� ����� �����Ǿ����ϴ�.");	
		return deleteRes;
	}
	
	

}
