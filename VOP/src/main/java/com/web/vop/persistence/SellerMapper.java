package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.SellerRequestDTO;
import com.web.vop.domain.SellerVO;
import com.web.vop.util.Pagination;

@Mapper
public interface SellerMapper {

	public List<SellerVO> selectRequestByState(@Param("requestState") String requestState, @Param("pagination") Pagination pagination);
	
	public int selectRequestByStateCnt(String requestState);
	
	public SellerVO selectRequestById(String memberId);
	
	public int insertRequest(SellerVO sellerVO);
	
	public int updateMemberContent(SellerVO sellerVO);

	public int updateAdminContent(SellerVO sellerVO);

	public int deleteRequest(String memberId);
	
	// 판매자 권한 요청 상세정보 조회
	public SellerRequestDTO selectSellerRequestDetails(String memberId);
}
