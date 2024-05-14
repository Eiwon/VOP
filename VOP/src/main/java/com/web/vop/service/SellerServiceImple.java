package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.SellerVO;
import com.web.vop.persistence.SellerMapper;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class SellerServiceImple implements SellerService{

	@Autowired
	SellerMapper sellerMapper;
	
	@Override
	public List<SellerVO> getRequestByState(String requestState, Pagination pagination) {
		log.info(requestState + "�� �Ǹ��� ���� ��û ��ȸ");
		List<SellerVO> result = sellerMapper.selectRequestByState(requestState, pagination);
		log.info("���� ��û �˻� ��� : " + result);
		return result;
	} // end getAllRequest

	@Override
	public int getRequestByStateCnt(String requestState) {
		log.info("���� ������� ��û �� ��ȸ");
		int res = sellerMapper.selectRequestByStateCnt(requestState);
		log.info("��û �� �˻� ��� : " + res);
		return res;
	} // end getRequestCount
	
	@Override
	public SellerVO getMyRequest(String memberId) {
		log.info("�ڽ��� ���� ��û ��ȸ");
		SellerVO sellerVO = sellerMapper.selectRequestById(memberId);
		log.info("��ȸ ��� : " + sellerVO);
		return sellerVO;
	} // end getMyRequest

	@Override
	public int registerRequest(SellerVO sellerVO) {
		log.info("�Ǹ��� ���� ��û ���");
		int res = sellerMapper.insertRequest(sellerVO);
		log.info(res + "�� �߰� ����");
		return res;
	} // end registerRequest

	@Override
	public int updateMemberContent(SellerVO sellerVO) {
		log.info("�ڽ��� ���� ��û ����");
		int res = sellerMapper.updateMemberContent(sellerVO);
		log.info(res + "�� ���� ����");
		return res;
	} // end updateMemberContent
	
	@Override
	public int refuseRequest(String memberId, String refuseMsg) {
		log.info("��û �ź�");
		int res = sellerMapper.updateAdminContent(memberId, refuseMsg);
		log.info(res + "�� ���� ����");
		return res;
	} // end refuseRequest

	@Override
	public int deleteRequest(String memberId) {
		log.info("��û ����");
		int res = sellerMapper.deleteRequest(memberId);
		log.info(res + "�� ���� ����");
		return res;
	} // end deleteRequest

	
}
