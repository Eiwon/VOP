package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.vop.domain.SellerVO;
import com.web.vop.util.Pagination;

@Mapper
public interface SellerMapper {

	public List<SellerVO> selectAllRequest(Pagination pagination);
	
	public int selectRequestCount();
	
	public SellerVO selectRequestById(String memberId);
	
	public int insertRequest(SellerVO sellerVO);
	
	public int updateMemberContent(SellerVO sellerVO);

	public int updateAdminContent(@RequestParam("memberId") String memberId, @RequestParam("refuseMsg") String refuseMsg);

	public int deleteRequest(String memberId);
}
