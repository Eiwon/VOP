package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.InquiryVO;


@Mapper
public interface InquiryMapper {
	
	 // ´ñ±Û(¸®ºä) µî·Ï
	 int insertInquiry(InquiryVO inquiryVO);
	 
	 // ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö
	 List<InquiryVO> selectListByInquiry(int productId);
	 
	 // ´ñ±Û(¸®ºä) ¼öÁ¤
	 int updateInquiry(InquiryVO inquiryVO);
	 
	 // ´ñ±Û(¸®ºä) »èÁ¦
	 int deleteInquiry(int productId, String memberId);
	 
}
