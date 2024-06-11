package com.web.vop.persistence;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.InquiryVO;
import com.web.vop.util.Pagination;



@Mapper
public interface InquiryMapper {
	
	 // ���(����) ���
	 int insertInquiry(InquiryVO inquiryVO);
	 
	 // ���(����) ��ü �˻�
	 List<InquiryVO> selectListByInquiry(int productId);
	 
	 // memberId�� ���� �˻� ����¡
	 List<InquiryVO> selectListByInquiryMemberIdPaging(@Param("memberId")String memberId, @Param("pagination") Pagination pagination);
	 
	// ���(����) memberId�� ����¡ �� �˻� �뵵
	 int selectListByInquiryMemberIdCnt(String memberId);
	 
	 // ���(����) ��ü �˻�
	 List<InquiryVO> selectListByInquiryPaging(@Param("productId")int productId, @Param("pagination") Pagination pagination);
	 
	 // ���(����) productId�� ����¡ �� �˻� �뵵
	 int selectListByInquiryCnt(int productId);
	 
	 // ���(����) productId �׸��� memberId ���� �˻�
	 InquiryVO selectByInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	 
	 // ���(����) ����
	 int updateInquiry(InquiryVO inquiryVO);
	 
	// ���(����) ����
	int deleteInquiry(@Param("productId")int productId, @Param("memberId")String memberId);
	 
}
