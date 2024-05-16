package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.InquiryVO;

public interface InquiryService {
	
		// ���(����) ���
		int createInquiry(InquiryVO inquiryVO);
			 
		// ���(����) ��ü �˻�
		List<InquiryVO> getAllInquiry(int productId);
			 
		// ���(����) ����
		int updateInquiry(int productId, String memberId, String inquiryContent);
			 
		// ���(����) ����
		int deleteInquiry(int productId, String memberId);
}
