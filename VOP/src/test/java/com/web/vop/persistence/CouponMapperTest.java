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
import com.web.vop.domain.CouponVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class CouponMapperTest {

	@Autowired
	public CouponMapper couponMapper;
	
	@Test
	public void test() {
		int couponId = 1;
	    String couponName = "test";
	    int discount = 20;
	    int publishing = 1;
	    String memberId = "test1234";
		Pagination pagination = new Pagination();
		CouponVO couponVO = new CouponVO();
		couponVO.setCouponId(couponId);
		couponVO.setCouponName(couponName);
		couponVO.setDiscount(discount);
		couponVO.setPublishing(publishing);
		
//		insertCouponTest(couponVO);
		selectAllCouponTest(pagination);
//		selectAllCouponCntTest();
//		deleteCouponByIdTest(couponId);
//		selectByIdTest(couponId);
		selectNotHadCouponTest(memberId);
//		updatePublishingByIdTest(couponId, publishing);
//		selectPublishingCouponTest();
		
	} // end test
	
	// �ű� ���� ���
	public void insertCouponTest(CouponVO couponVO) {
	    log.info(couponMapper.insertCoupon(couponVO));
	} // end insertCouponTest

	// ��� ���� ��ȸ (����¡)
	public void selectAllCouponTest(Pagination pagination) {
	    log.info(couponMapper.selectAllCoupon(pagination));
	} // end selectAllCouponTest

	// ��� ���� �� ��ȸ
	public void selectAllCouponCntTest() {
	    log.info(couponMapper.selectAllCouponCnt());
	} // end selectAllCouponCntTest

	// ���� ����
	public void deleteCouponByIdTest(int couponId) {
	    log.info(couponMapper.deleteCouponById(couponId));
	} // end deleteCouponByIdTest

	// id�� ���� �˻�
	public void selectByIdTest(int couponId) {
	    log.info(couponMapper.selectById(couponId));
	} // end selectByIdTest

	// ���� �������� ���� or ������� ���� ���� �˻�
	public void selectNotHadCouponTest(String memberId) {
	    log.info(couponMapper.selectNotHadCoupon(memberId));
	} // end selectNotHadCouponTest

	// ���� ���� ����
	public void updatePublishingByIdTest(int couponId, int publishing) {
	    log.info(couponMapper.updatePublishingById(couponId, publishing));
	} // end updatePublishingByIdTest

	// ���� ���� ��� ���� �˻�
	public void selectPublishingCouponTest() {
	    log.info(couponMapper.selectPublishingCoupon());
	} // end selectPublishingCouponTest
	
}
