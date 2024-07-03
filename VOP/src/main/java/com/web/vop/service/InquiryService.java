package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.InquiryDTO;
import com.web.vop.domain.InquiryVO;
import com.web.vop.util.PageMaker;

public interface InquiryService {
	
		// ���(����) ���
		int createInquiry(InquiryVO inquiryVO);
			 
		// ���(����) ��ü �˻�
		List<InquiryVO> getAllInquiry(int productId);
		
		List<InquiryDTO>getAllMyInquiryDTO(String memberId, PageMaker pageMaker);
		
		// ���(����) ȸ�� ����¡ ��ü �˻�
		List<InquiryVO>getAllInquiryMemberIdPaging(String memberId, PageMaker pageMaker);
		
		// ���(����) ��ǰ ����¡ ��ü �˻�
		List<InquiryVO> getAllInquiryPaging(int productId, PageMaker pageMaker);
			 
		// ���(����) �˻�
		InquiryVO selectByInquiry(int productId, String memberId);
		
		// ���(����) ����
		int updateInquiry(int productId, String memberId, String inquiryContent);
			 
		// ���(����) ����
		int deleteInquiry(int productId, String memberId);
}
