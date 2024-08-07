package com.web.vop.persistence;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.InquiryDTO;
import com.web.vop.domain.InquiryVO;
import com.web.vop.util.Pagination;



@Mapper
public interface InquiryMapper {
	
	 // 댓글(문의) 등록
	 int insertInquiry(InquiryVO inquiryVO);
	 
	 // 댓글(문의) 전체 검색
	 List<InquiryVO> selectListByInquiry(int productId);
	 
	 // memberId로 문의 검색 페이징
	 List<InquiryDTO> selectListByInquiryMemberIdPagingNew(@Param("memberId")String memberId, @Param("pagination") Pagination pagination);
	 
	 // memberId로 문의 검색 페이징
	 List<InquiryVO> selectListByInquiryMemberIdPaging(@Param("memberId")String memberId, @Param("pagination") Pagination pagination);
	 
	// 댓글(문의) memberId로 페이징 수 검색 용도
	 int selectListByInquiryMemberIdCnt(String memberId);
	 
	 // productId로 댓글(문의) 전체 검색
	 List<InquiryVO> selectListByInquiryPaging(@Param("productId")int productId, @Param("pagination") Pagination pagination);
	 
	 // 댓글(문의) productId로 페이징 수 검색 용도
	 int selectListByInquiryCnt(int productId);
	 
	 // 댓글(문의) productId 그리고 memberId 통해 검색
	 InquiryVO selectByInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // 댓글(문의) 수정
	 int updateInquiry(InquiryVO inquiryVO);
	 
	// 댓글(문의) 삭제
	int deleteInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	
	// 관리자로 댓글(문의) 전체 검색
	List<InquiryVO> allList(@Param("pagination") Pagination pagination);
	
	// 댓글(문의) productId로 페이징 수 검색 용도
	int allListPaging();

}
