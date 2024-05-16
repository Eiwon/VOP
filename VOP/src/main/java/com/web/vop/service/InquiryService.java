package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.InquiryVO;

public interface InquiryService {
	
		// 댓글(문의) 등록
		int createInquiry(InquiryVO inquiryVO);
			 
		// 댓글(문의) 전체 검색
		List<InquiryVO> getAllInquiry(int productId);
			 
		// 댓글(문의) 수정
		int updateInquiry(int productId, String memberId, String inquiryContent);
			 
		// 댓글(문의) 삭제
		int deleteInquiry(int productId, String memberId);
}
