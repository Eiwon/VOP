package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.MessageVO;
import com.web.vop.domain.SellerRequestDTO;
import com.web.vop.domain.SellerVO;
import com.web.vop.util.PageMaker;

public interface SellerService {

	public List<SellerVO> getRequestByState(String requestState, PageMaker pageMaker);
	
	public SellerVO getMyRequest(String memberId);
	
	public int registerRequest(SellerVO sellerVO);
	
	public int updateMemberContent(SellerVO sellerVO);
	
	public int approveRequest(SellerVO sellerVO);
	
	public int deleteRequest(String memberId);
	
	public SellerRequestDTO getSellerRequestDetails(String memberId);
	
	// 판매자 권한 취소
	public int revokeAuth(SellerVO sellerVO);
	
	// 판매자 권한 재신청
	public int retrySellerRequest(SellerVO sellerVO);
}
