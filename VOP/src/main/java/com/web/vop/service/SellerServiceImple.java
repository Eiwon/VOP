package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.MessageVO;
import com.web.vop.domain.SellerVO;
import com.web.vop.persistence.Constant;
import com.web.vop.persistence.MemberMapper;
import com.web.vop.persistence.MessageMapper;
import com.web.vop.persistence.SellerMapper;
import com.web.vop.util.PageMaker;
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
	MessageMapper messageMapper;
	
	@Override
	public List<SellerVO> getRequestByState(String requestState, PageMaker pageMaker) {
		log.info(requestState + "인 판매자 권한 요청 조회");
		int totalCnt = sellerMapper.selectRequestByStateCnt(requestState);
		pageMaker.setTotalCount(totalCnt);
		List<SellerVO> result = sellerMapper.selectRequestByState(requestState, pageMaker.getPagination());
		log.info("권한 요청 검색 결과 : " + result);
		return result;
	} // end getAllRequest
	
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
		sellerVO.setRequestState(Constant.STATE_APPROVAL_WAIT);
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
		if(sellerVO.getRequestState().equals(Constant.STATE_APPROVED)) { // 승인되었다면 회원 권한 변경
			memberMapper.updateMemberAuth(sellerVO.getMemberId(), Constant.AUTH_SELLER);
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
	public int registerNotice(MessageVO messageVO) {
		log.info("공지사항 등록");
		int res = messageMapper.insertMessage(messageVO);
		return res;
	} // end registerNotice

	
}
