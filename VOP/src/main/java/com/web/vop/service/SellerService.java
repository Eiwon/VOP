package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.SellerVO;
import com.web.vop.util.Pagination;

public interface SellerService {

	public List<SellerVO> getRequestByState(String requestState, Pagination pagination);

	public int getRequestByStateCnt(String requestState);
	
	public SellerVO getMyRequest(String memberId);
	
	public int registerRequest(SellerVO sellerVO);
	
	public int updateMemberContent(SellerVO sellerVO);
	
	public int approveRequest(SellerVO sellerVO);
	
	public int deleteRequest(String memberId);
	
	public int deleteProductRequest(int productId);
	
}
