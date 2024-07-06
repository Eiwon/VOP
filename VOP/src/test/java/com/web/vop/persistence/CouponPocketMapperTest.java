package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.web.vop.config.RootConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.CouponPocketVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class CouponPocketMapperTest {

	@Autowired
	public CouponPocketMapper couponPocketMapper;
	
	@Test
	public void test() {
		String memberId = "test1234";
		int couponId = 2;
		int isUsed = 1;
		
		selectByMemberIdTest(memberId);
		selectUsableByMemberIdTest(memberId);
		insertCouponPocketTest(couponId, memberId);
		updateIsUsedTest(couponId, memberId, isUsed);
		deleteCouponByIdTest(couponId, memberId);
		selectIdByIdTest(couponId, memberId);
		
	} // end test
	
	// memberId�� ���� ��ȸ
	public void selectByMemberIdTest(String memberId) {
	    log.info(couponPocketMapper.selectByMemberId(memberId));
	} // end selectByMemberIdTest

	// memberId�� ��� ������ ���� ��ȸ
	public void selectUsableByMemberIdTest(String memberId) {
	    log.info(couponPocketMapper.selectUsableByMemberId(memberId));
	} // end selectUsableByMemberIdTest

	// ���� �߰�
	public void insertCouponPocketTest(int couponId, String memberId) {
	    log.info(couponPocketMapper.insertCouponPocket(couponId, memberId));
	} // end insertCouponPocketTest

	// ���� Ȱ��ȭ / ��Ȱ��ȭ
	public void updateIsUsedTest(int couponId, String memberId, int isUsed) {
	    log.info(couponPocketMapper.updateIsUsed(couponId, memberId, isUsed));
	} // end updateIsUsedTest

	// ���� ���� ����
	public void deleteCouponByIdTest(int couponId, String memberId) {
	    log.info(couponPocketMapper.deleteCouponById(couponId, memberId));
	} // end deleteCouponByIdTest

	// ���� ���� �������� Ȯ��
	public void selectIdByIdTest(int couponId, String memberId) {
	    log.info(couponPocketMapper.selectIdById(couponId, memberId));
	} // end selectIdByIdTest
}
