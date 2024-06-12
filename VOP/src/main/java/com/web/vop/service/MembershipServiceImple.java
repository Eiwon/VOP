package com.web.vop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.MembershipVO;
import com.web.vop.persistence.MembershipMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MembershipServiceImple implements MembershipService{
	
	@Autowired
	MembershipMapper membershipMapper;

	
	@Override
	public int registerMembership(String memberId) { //����� ���
		log.info("registerMembership : " + memberId);
		int res = membershipMapper.insertMembership(memberId);
		if(res == 1) {
			log.info("����� ��� ����");
		}else {
			log.info("����� ��� ����");
		}
		return res;
	}//end registerMembership() 

	
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
