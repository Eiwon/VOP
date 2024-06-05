package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.InquiryVO;
import com.web.vop.util.PageMaker;

public interface InquiryService {
	
		// ´ñ±Û(¹®ÀÇ) µî·Ï
		int createInquiry(InquiryVO inquiryVO);
			 
		// ´ñ±Û(¹®ÀÇ) ÀüÃ¼ °Ë»ö
		List<InquiryVO> getAllInquiry(int productId);
		
		List<InquiryVO>getAllInquiryMemberIdPaging(String memberId, PageMaker pageMaker);
		
		// ´ñ±Û(¹®ÀÇ) ÀüÃ¼ °Ë»ö
		List<InquiryVO> getAllInquiryPaging(int productId, PageMaker pageMaker);
			 
		// ´ñ±Û(¸®ºä) °Ë»ö
		InquiryVO selectByInquiry(int productId, String memberId);
		
		// ´ñ±Û(¹®ÀÇ) ¼öÁ¤
		int updateInquiry(int productId, String memberId, String inquiryContent);
			 
		// ´ñ±Û(¹®ÀÇ) »èÁ¦
		int deleteInquiry(int productId, String memberId);
}
