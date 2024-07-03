package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.InquiryDTO;
import com.web.vop.domain.InquiryVO;
import com.web.vop.util.PageMaker;

public interface InquiryService {
	
		// 댓글(문의) 등록
		int createInquiry(InquiryVO inquiryVO);
			 
		// 댓글(문의) 전체 검색
		List<InquiryVO> getAllInquiry(int productId);
		
		List<InquiryDTO>getAllMyInquiryDTO(String memberId, PageMaker pageMaker);
		
		// 댓글(문의) 회원 페이징 전체 검색
		List<InquiryVO>getAllInquiryMemberIdPaging(String memberId, PageMaker pageMaker);
		
		// 댓글(문의) 상품 페이징 전체 검색
		List<InquiryVO> getAllInquiryPaging(int productId, PageMaker pageMaker);
			 
		// 댓글(리뷰) 검색
		InquiryVO selectByInquiry(int productId, String memberId);
		
		// 댓글(문의) 수정
		int updateInquiry(int productId, String memberId, String inquiryContent);
			 
		// 댓글(문의) 삭제
		int deleteInquiry(int productId, String memberId);
}
