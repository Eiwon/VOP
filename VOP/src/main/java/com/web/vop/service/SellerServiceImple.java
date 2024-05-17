package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.SellerVO;
import com.web.vop.persistence.MemberMapper;
import com.web.vop.persistence.SellerMapper;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class SellerServiceImple implements SellerService{

	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	SellerMapper sellerMapper;
	
	@Autowired
	ProductService productService;
	
	@Override
	public List<SellerVO> getRequestByState(String requestState, Pagination pagination) {
		log.info(requestState + "인 판매자 권한 요청 조회");
		List<SellerVO> result = sellerMapper.selectRequestByState(requestState, pagination);
		log.info("권한 요청 검색 결과 : " + result);
		return result;
	} // end getAllRequest

	@Override
	public int getRequestByStateCnt(String requestState) {
		log.info("승인 대기중인 요청 수 조회");
		int res = sellerMapper.selectRequestByStateCnt(requestState);
		log.info("요청 수 검색 결과 : " + res);
		return res;
	} // end getRequestCount
	
	@Override
	public SellerVO getMyRequest(String memberId) {
		log.info("자신의 권한 요청 조회");
		SellerVO sellerVO = sellerMapper.selectRequestById(memberId);
		log.info("조회 결과 : " + sellerVO);
		return sellerVO;
	} // end getMyRequest

	@Override
	public int registerRequest(SellerVO sellerVO) {
		log.info("판매자 권한 요청 등록");
		int res = sellerMapper.insertRequest(sellerVO);
		log.info(res + "행 추가 성공");
		return res;
	} // end registerRequest

	@Override
	public int updateMemberContent(SellerVO sellerVO) {
		log.info("자신이 보낸 요청 수정");
		int res = sellerMapper.updateMemberContent(sellerVO);
		log.info(res + "행 수정 성공");
		return res;
	} // end updateMemberContent
	
	@Override
	public int approveRequest(SellerVO sellerVO) {
		log.info("판매자 권한 부여 / 회수");
		int res = sellerMapper.updateAdminContent(sellerVO);
		log.info(res + "행 수정 성공");
		if(sellerVO.getRequestState().equals("승인")) { // 승인되었다면 회원 권한 변경
			memberMapper.updateMemberAuth(sellerVO.getMemberId(), "판매자");
		}else {
			memberMapper.updateMemberAuth(sellerVO.getMemberId(), "일반");
		}
		return res;
	} // end approveRequest

	@Override
	public int deleteRequest(String memberId) {
		log.info("요청 삭제");
		int res = sellerMapper.deleteRequest(memberId);
		log.info(res + "행 삭제 성공");
		return res;
	} // end deleteRequest

	@Override
	public int deleteProductRequest(int productId) {
		log.info("상품 삭제 요청");
		// 판매 중인 상품이면 삭제 대기중 상태로 전환, (판매 중, 삭제 대기중) 상태가 아니라면 즉시 삭제
		String productState = productService.selectStateByProductId(productId);
		log.info("현재 상태 : " + productState);

		int res = 0;
		if (productState.equals("판매중")) {
			if (productService.setProductState("삭제 대기중", productId) == 1)
				res = 1;
		} else if (!productState.equals("삭제 대기중")) {
			if (productService.deleteProduct(productId) == 1)
				res = 2;
		}
		return res;
	} // end deleteProductRequest

	

	
}
