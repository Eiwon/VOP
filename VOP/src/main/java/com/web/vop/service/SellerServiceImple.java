package com.web.vop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.SellerVO;
import com.web.vop.util.Pagination;

@Service
public class SellerServiceImple implements SellerService{

	@Override
	public List<SellerVO> getAllRequest(Pagination pagination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SellerVO getMyRequest(String memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int registerRequest(SellerVO sellerVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateMemberContent(SellerVO sellerVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int refuseRequest(String memberId, String refuseMsg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteRequest(String memberId) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
