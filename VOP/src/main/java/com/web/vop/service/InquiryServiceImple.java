package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.InquiryVO;
import com.web.vop.domain.ProductVO;
import com.web.vop.persistence.AnswerMapper;
import com.web.vop.persistence.InquiryMapper;
import com.web.vop.persistence.ProductMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class InquiryServiceImple implements InquiryService{
	
	@Autowired
	private InquiryMapper inquiryMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	
	// ���(����) ���
	@Override
	public int createInquiry(InquiryVO inquiryVO) {
		log.info("createInquiry()");
		
		int productId = inquiryVO.getProductId();
		
		String memberId = inquiryVO.getMemberId();
		
		log.info("productId : " + productId);
		log.info("memberId : " + memberId);
		
		ProductVO productVO = productMapper.selectProduct(productId);
		
		log.info("productVO : " + productVO);
		
		int insertRes = 2;
		
		if(productVO != null) {
			// ���� ����� �ִ��� ������ Ȯ��
			InquiryVO vo = inquiryMapper.selectByInquiry(productId, memberId);
			
			log.info("vo" + vo);
			
			insertRes = 0;
			
			if(vo == null) {
				insertRes = inquiryMapper.insertInquiry(inquiryVO);
				log.info(insertRes + "�� ����(���) ���");
			}else {
				log.info(memberId + "�� ����(���)�� �̹� �ֽ��ϴ�.");
			}
		}

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
		
		InquiryVO vo = inquiryMapper.selectByInquiry(productId, memberId);
		
		int updateRes = 0;
		if(vo != null) {
			InquiryVO inquiryVO = new InquiryVO();
			log.info("inquiryVO : " + inquiryVO);
			inquiryVO.setProductId(productId);
			inquiryVO.setMemberId(memberId);
			inquiryVO.setInquiryContent(inquiryContent);
			updateRes = inquiryMapper.updateInquiry(inquiryVO);
			log.info(updateRes + "�� ����� �����Ǿ����ϴ�.");
		}
		
		return updateRes;
	}
	
	// ���(����) ����

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
