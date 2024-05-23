package com.web.vop.persistence;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.InquiryVO;



@Mapper
public interface InquiryMapper {
	
	 // 댓글(문의) 등록
	 int insertInquiry(InquiryVO inquiryVO);
	 
	 // 댓글(문의) 전체 검색
	 List<InquiryVO> selectListByInquiry(int productId);

	 // 댓글(문의) productId 그리고 memberId 통해 검색
	 InquiryVO selectByInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // 댓글(문의) 수정
	 int updateInquiry(InquiryVO inquiryVO);
	 
	// 댓글(문의) 삭제
	int deleteInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	 
}
