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
	
	
	// 댓글(문의) 등록
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
			// 문의 등록이 있는지 없는지 확인
			InquiryVO vo = inquiryMapper.selectByInquiry(productId, memberId);
			
			log.info("vo" + vo);
			
			insertRes = 0;
			
			if(vo == null) {
				insertRes = inquiryMapper.insertInquiry(inquiryVO);
				log.info(insertRes + "행 문의(댓글) 등록");
			}else {
				log.info(memberId + "님 문의(댓글)는 이미 있습니다.");
			}
		}

		return insertRes;
	}
	
	// 댓글(문의) 전체 검색
	@Override
	public List<InquiryVO> getAllInquiry(int productId) {
		log.info("getAllInquiry()");
		List<InquiryVO> result = inquiryMapper.selectListByInquiry(productId);
		return result;
	}
	
	// 댓글(문의) 검색
	@Override
	public InquiryVO selectByInquiry(int productId, String memberId) {
		log.info("selectByInquiry()");
		InquiryVO inquiryVO = inquiryMapper.selectByInquiry(productId, memberId);
		return inquiryVO;
	}
	
	// 댓글(문의) 수정
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
			log.info(updateRes + "행 댓글이 수정되었습니다.");
		}
		
		return updateRes;
	}
	
	// 댓글(문의) 삭제

	@Override
	public int deleteInquiry(int productId, String memberId) {
		log.info("deleteInquiry()");
		log.info("productId : " + productId);
		log.info("memberId : " + memberId);
		
		int deleteRes = inquiryMapper.deleteInquiry(productId, memberId);
	
		log.info(deleteRes + "행 댓글이 삭제되었습니다.");	
		return deleteRes;
	}
	
	

}
