package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.InquiryVO;


@Mapper
public interface InquiryMapper {
	
	 // ���(����) ���
	 int insertInquiry(InquiryVO inquiryVO);
	 
	 // ���(����) ��ü �˻�
	 List<InquiryVO> selectListByInquiry(int productId);
	 
	 // ���(����) ����
	 int updateInquiry(InquiryVO inquiryVO);
	 
	 // ���(����) ����
	 int deleteInquiry(int productId, String memberId);
	 
}
