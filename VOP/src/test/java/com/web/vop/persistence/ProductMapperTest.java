package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // ������ JUnit test ����
@ContextConfiguration(classes = {RootConfig.class}) // ���� ���� ����
@Log4j
public class ProductMapperTest {

	@Autowired
	private ProductMapper productMapper;
	
	@Test
	public void test() {
//		testProductByProductId(); // �׽�Ʈ �Ϸ�
//		testSelectReviewByCount(); // �׽�Ʈ �Ϸ�
		testSelectReviewByStar(); // �׽�Ʈ �Ϸ�
//		testSelectByMainImg(); // ��� �Ǵ� �ڵ�
	}
	
	// ��ǰ �� �˻� �׽�Ʈ
	private void testProductByProductId() {
		ProductVO productVO = productMapper.selectProduct(1);
		log.info("testProductByProductId() : " + productVO);
	}
	
	// ��� �� ���� ��ȸ �׽�Ʈ
	private void testSelectReviewByCount() {
		int res = productMapper.selectReviewCount(4);
		log.info("testSelectReviewByCount() : " + res);
	}
	
	// ���� �� ��
	private void testSelectReviewByStar() {
		int res = productMapper.selectReviewStar(2);
		log.info("testSelectReviewByStar() : " + res);
	}
	
//	// �̹��� �� �˻� �׽�Ʈ
//	private void testSelectByMainImg() {
//		ProductVO ProductVO = productMapper.selectByMainImg(2);
//		log.info("testImageByImageId()" + ProductVO);
//	}
	
}
