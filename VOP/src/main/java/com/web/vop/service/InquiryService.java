package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.InquiryVO;
import com.web.vop.util.PageMaker;

public interface InquiryService {
	
		// ���(����) ���
		int createInquiry(InquiryVO inquiryVO);
			 
		// ���(����) ��ü �˻�
		List<InquiryVO> getAllInquiry(int productId);
		
		List<InquiryVO>getAllInquiryMemberIdPaging(String memberId, PageMaker pageMaker);
		
		// ���(����) ��ü �˻�
		List<InquiryVO> getAllInquiryPaging(int productId, PageMaker pageMaker);
			 
		// ���(����) �˻�
		InquiryVO selectByInquiry(int productId, String memberId);
		
		// ���(����) ����
		int updateInquiry(int productId, String memberId, String inquiryContent);
			 
		// ���(����) ����
		int deleteInquiry(int productId, String memberId);
}
