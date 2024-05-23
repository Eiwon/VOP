package com.web.vop.persistence;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.InquiryVO;



@Mapper
public interface InquiryMapper {
	
	 // ���(����) ���
	 int insertInquiry(InquiryVO inquiryVO);
	 
	 // ���(����) ��ü �˻�
	 List<InquiryVO> selectListByInquiry(int productId);

	 // ���(����) productId �׸��� memberId ���� �˻�
	 InquiryVO selectByInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // ���(����) ����
	 int updateInquiry(InquiryVO inquiryVO);
	 
	// ���(����) ����
	int deleteInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	 
}
