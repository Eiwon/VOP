package com.web.vop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.vop.domain.MemberVO;
import com.web.vop.domain.MembershipVO;
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
	public int getNextMembershipId() { // membershipId 생성
		log.info("getNextMembershipId()");
		paymentMapper.updatePaymentSeq();
		return paymentMapper.selectNextPaymentId();
	}

	
	@Override
	public PaymentWrapper makeMembershipForm(String memberId) {
		PaymentWrapper payment = new PaymentWrapper();
		MembershipVO vo = new MembershipVO();
		log.info(vo);
		
		int membershipId = 0; // int 타입 변수 초기화
		
		// 새 membershipId 생성
		paymentMapper.updatePaymentSeq();
		membershipId = paymentMapper.selectNextPaymentId();
		//membershipId = membershipMapper.selectNextMembershipId();
		log.info("membershipId : " + membershipId);
		
		vo.setMembershipId(membershipId);
		vo.setMemberId(memberId);
		log.info("입력된 membershipVO - " + vo);
		
		// 유저 정보 등록
		MemberVO memberVO = memberMapper.selectByMemberId(memberId);
		log.info("검색된 유저 정보 : " + memberVO);
		payment.setMemberVO(memberVO);
		
		return payment;
	}//end makeMembershipForm()
	
	
	@Transactional(value = "transactionManager")// 트랜잭션
	@Override
	public int registerMembership(PaymentWrapper payment)throws DataIntegrityViolationException{
		log.info("registerMembership()");
		int res = 0;
		MembershipVO membershipVO = payment.getMembershipVO();
		// 결제 정보에서 chargeId 설정
		String chargeId = payment.getMembershipVO().getChargeId();
		// membershipVO에 chargeId 설정
	    membershipVO.setChargeId(chargeId);
	    
		log.info("chargeId ->> " + membershipVO);
		
		String memberId = membershipVO.getMemberId();
		log.info("memberId : " + memberId);
		
		res = membershipMapper.insertMembership(membershipVO);
		
		// 회원 권한 수정
		membershipMapper.updateMemberAuthOnInsert(memberId);
		
		return res;
	}//end registerMembership()
	

	@Override
	public void updateMemberAuth(String memberId) { //멤버십 권한 업데이트
//		log.info("updateMemberAuth : " + memberId);
//		membershipMapper.updateMemberAuthOnInsert(memberId);
//		log.info("멤버십 권한 업데이트(멤버십)가 성공했습니다. " + memberId);
	}//end updateMemberAuth()

	
	@Override
	public MembershipVO selectByMemberId(String memberId) { // 멤버십 전체 조회 
		log.info("selectByMemberId : " + memberId);
		MembershipVO result = membershipMapper.selectByMemberId(memberId);
		//log.info("멤버십 전체 조회 : " + result.toString());
		return result;
	}//end selectByMemberId()

	
	@Override
	public Date getExpiryDate(String memberId) { // 멤버십 만료일 조회
		log.info("getExpiryDate(). " + memberId);
		Date date = membershipMapper.selectExpiryDate(memberId);
		log.info("멤버십 만료 기간 : " + date);
		return date;
	}//end getExpiryDate()
	
	
	@Override
	public int deleteMembership(String memberId) { // 멤버십 삭제
		log.info("deleteMembership : " + memberId);
		int res = membershipMapper.deleteMembership(memberId);
		if(res == 1) {
			log.info("멤버십 삭제 성공");
		}else {
			log.info("멤버십 삭제 실패");
		}
		return res;
	}//end deleteMembership()


	@Override
	public void updateMemberAuthOnDelete(String memberId) { // 멤버십 권한(일반유저) 변경
		log.info("updateMemberAuthOnDelete : " + memberId);
		membershipMapper.updateMemberAuthOnDelete(memberId);
		log.info("멤버십 권한 업데이트(일반유저)가 성공했습니다. " + memberId);
	}//end updateMemberAuthOnDelete()


	@Override
	public String getChargeIdByMemberId(String memberId) { // 멤버십 환불아이디(chargeId) 조회
		log.info("getChargeIdByMemberId : " + memberId);
		String chargeId = membershipMapper.selectChargeIdByMemberId(memberId);
		log.info("환불아이디 : " + chargeId);
		return chargeId;
	}//end getChargeIdByMemberId()


	
	
}//end MembershipServiceImple()
