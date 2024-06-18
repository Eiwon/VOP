package com.web.vop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.MemberVO;
import com.web.vop.domain.MembershipVO;
import com.web.vop.domain.PaymentVO;
import com.web.vop.domain.PaymentWrapper;
import com.web.vop.persistence.MemberMapper;
import com.web.vop.persistence.MembershipMapper;
import com.web.vop.persistence.PaymentMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MembershipServiceImple implements MembershipService{
	
	@Autowired
	MembershipMapper membershipMapper;

	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	PaymentMapper paymentMapper;
	
	@Override
	public int getNextMembershipId() { // membershipId ����
		log.info("getNextMembershipId()");
		return  paymentMapper.selectNextPaymentId();
	}

	
	@Override
	public PaymentWrapper makeMembershipForm(String memberId) {
		PaymentWrapper payment = new PaymentWrapper();
		MembershipVO vo = new MembershipVO();
		log.info(vo);
		
		int membershipId = 0; // int Ÿ�� ���� �ʱ�ȭ
		
		// �� membershipId ����
		membershipId = membershipMapper.selectNextMembershipId();
		log.info("membershipId : " + membershipId);
		
		vo.setMembershipId(membershipId);
		vo.setMemberId(memberId);
		log.info("�Էµ� membershipVO - " + vo);
		
		// ���� ���� ���
		MemberVO memberVO = memberMapper.selectByMemberId(memberId);
		log.info("�˻��� ���� ���� : " + memberVO);
		payment.setMemberVO(memberVO);
		
		return payment;
	}//end makeMembershipForm()
	
	
	//@Transactional(value = "transactionManager")
	@Override
	public int registerMembership(PaymentWrapper payment)throws DataIntegrityViolationException{
		log.info("registerMembership()");
		int res = 0;
		MembershipVO membershipVO = payment.getMembershipVO();
		// ���� �������� chargeId ����
		String chargeId = payment.getMembershipVO().getChargeId();
		// membershipVO�� chargeId ����
	    membershipVO.setChargeId(chargeId);
	    
		log.info("chargeId ->> " + membershipVO);
		
		res = membershipMapper.insertMembership(membershipVO);
		
		return res;
	}//end registerMembership()
	
	
	
	/*
	@Override
	public int registerMembership(MembershipVO membershipVO) {
		log.info("registerMembership() :" + membershipVO);
		int res = membershipMapper.insertMembership(membershipVO);
		if(res == 1) {
			log.info("����� ��� ����!");
		}else {
			log.info("����� ��� ����!");
		}
		return res;
	}*/

	
	@Override
	public void updateMemberAuth(String memberId) { //����� ���� ������Ʈ
		log.info("updateMemberAuth : " + memberId);
		membershipMapper.updateMemberAuthOnInsert(memberId);
		log.info("����� ���� ������Ʈ(�����)�� �����߽��ϴ�. " + memberId);
	}//end updateMemberAuth()

	
	@Override
	public MembershipVO selectByMemberId(String memberId) { // ����� ��ü ��ȸ 
		log.info("selectByMemberId : " + memberId);
		MembershipVO result = membershipMapper.selectByMemberId(memberId);
		log.info("����� ��ü ��ȸ : " + result.toString());
		return result;
	}

	
	@Override
	public Date getExpiryDate(String memberId) { // ����� ������ ��ȸ
		log.info("getExpiryDate(). " + memberId);
		Date date = membershipMapper.selectExpiryDate(memberId);
		log.info("����� ���� �Ⱓ : " + date);
		return date;
	}
	
	
	@Override
	public int deleteMembership(String memberId) { // ����� ����
		log.info("deleteMembership : " + memberId);
		int res = membershipMapper.deleteMembership(memberId);
		if(res == 1) {
			log.info("����� ���� ����");
		}else {
			log.info("����� ���� ����");
		}
		return res;
	}


	@Override
	public void updateMemberAuthOnDelete(String memberId) { // ����� ����(�Ϲ�����) ����
		log.info("updateMemberAuthOnDelete : " + memberId);
		membershipMapper.updateMemberAuthOnDelete(memberId);
		log.info("����� ���� ������Ʈ(�Ϲ�����)�� �����߽��ϴ�. " + memberId);
	}


	
	
}//end MembershipServiceImple()
