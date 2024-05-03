package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.SellerVO;
import com.web.vop.util.Pagination;

public interface SellerService {

	public List<SellerVO> getAllRequest(Pagination pagination);

	public int getRequestCount();
	
	public SellerVO getMyRequest(String memberId);
	
	public int registerRequest(SellerVO sellerVO);
	
	public int updateMemberContent(SellerVO sellerVO);
	
	public int refuseRequest(String memberId, String refuseMsg);
	
	public int deleteRequest(String memberId);
}
