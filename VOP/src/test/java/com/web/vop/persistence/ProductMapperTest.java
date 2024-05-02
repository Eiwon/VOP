package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
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
		testProductByProductId();
	}
	
	// ��ǰ �� �˻� �׽�Ʈ
	private void testProductByProductId() {
		ProductVO productVO = productMapper.selectProduct(1);
		log.info("testProductByProductId() : " + productVO);
	}
	
}
