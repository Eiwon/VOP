package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.web.vop.config.RootConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.MembershipVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class MembershipMapperTest {
	
	@Autowired
	private MembershipMapper membershipMapper;
	 
	@Test
	public void test() {
		 MembershipVO membershipVO = new MembershipVO();
	     String memberId = "test1234";
	     membershipVO.setChargeId("1");
	     membershipVO.setMemberId(memberId);
	     membershipVO.setMembershipFee(1000);
	     membershipVO.setMembershipId(1300);
	     
	     try {
	         insertMembershipTest(membershipVO);
	     } catch (DataIntegrityViolationException e) {
	         log.error(e);
	     }
	     
	     selectNextMembershipIdTest();
	     updateMemberAuthOnInsertTest(memberId);
	     selectByMemberIdTest(memberId);
	     selectValidByMemberIdTest(memberId);
	     selectExpiryDateTest(memberId);
	     deleteMembershipTest(memberId);
	     updateMemberAuthOnDeleteTest(memberId);
	     selectExpiryDateBySchedullingTest();
	}
	
	// MembershipId ����
    public void selectNextMembershipIdTest() {
        log.info(membershipMapper.selectNextMembershipId());
    } // end selectNextMembershipIdTest

    // ����� ��� 
    public void insertMembershipTest(MembershipVO membershipVO) throws DataIntegrityViolationException {
        log.info(membershipMapper.insertMembership(membershipVO));
    } // end insertMembershipTest

    // ����� ���� ������Ʈ 
    public void updateMemberAuthOnInsertTest(String memberId) {
        log.info(membershipMapper.updateMemberAuthOnInsert(memberId));
    } // end updateMemberAuthOnInsertTest

    // ����� ��ü ��ȸ
    public void selectByMemberIdTest(String memberId) {
        log.info(membershipMapper.selectByMemberId(memberId));
    } // end selectByMemberIdTest

    // memberId�� ������� ���� ����ʸ� ��ȸ
    public void selectValidByMemberIdTest(String memberId) {
        log.info(membershipMapper.selectValidByMemberId(memberId));
    } // end selectValidByMemberIdTest

    // ����� ������ ��ȸ
    public void selectExpiryDateTest(String memberId) {
        log.info(membershipMapper.selectExpiryDate(memberId));
    } // end selectExpiryDateTest

    // ����� ����(����)
    public void deleteMembershipTest(String memberId) {
        log.info(membershipMapper.deleteMembership(memberId));
    } // end deleteMembershipTest

    // ����� ���� �� ���� ������Ʈ
    public void updateMemberAuthOnDeleteTest(String memberId) {
        log.info(membershipMapper.updateMemberAuthOnDelete(memberId));
    } // end updateMemberAuthOnDeleteTest

    // ����� ������ ��ȸ (�����ٸ�)
    public void selectExpiryDateBySchedullingTest() {
        log.info(membershipMapper.selectExpiryDateBySchedulling());
    } // end selectExpiryDateBySchedullingTest
    
}
