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
	public int approveRequest(SellerVO sellerVO) {
		log.info("�Ǹ��� ���� �ο� / ȸ��");
		int res = sellerMapper.updateAdminContent(sellerVO);
		log.info(res + "�� ���� ����");
		if(sellerVO.getRequestState().equals("����")) { // ���εǾ��ٸ� ȸ�� ���� ����
			memberMapper.updateMemberAuth(sellerVO.getMemberId(), "�Ǹ���");
		}else {
			memberMapper.updateMemberAuth(sellerVO.getMemberId(), "�Ϲ�");
		}
		return res;
	} // end approveRequest

	@Override
	public int deleteRequest(String memberId) {
		log.info("��û ����");
		int res = sellerMapper.deleteRequest(memberId);
		log.info(res + "�� ���� ����");
		return res;
	} // end deleteRequest

	@Override
	public int deleteProductRequest(int productId) {
		log.info("��ǰ ���� ��û");
		// �Ǹ� ���� ��ǰ�̸� ���� ����� ���·� ��ȯ, (�Ǹ� ��, ���� �����) ���°� �ƴ϶�� ��� ����
		String productState = productService.selectStateByProductId(productId);
		log.info("���� ���� : " + productState);

		int res = 0;
		if (productState.equals("�Ǹ���")) {
			if (productService.setProductState("���� �����", productId) == 1)
				res = 1;
		} else if (!productState.equals("���� �����")) {
			if (productService.deleteProduct(productId) == 1)
				res = 2;
		}
		return res;
	} // end deleteProductRequest

	

	
}
