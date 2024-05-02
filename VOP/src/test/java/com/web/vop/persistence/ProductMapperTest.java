package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.domain.ProductVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 JUnit test 연결
@ContextConfiguration(classes = {RootConfig.class}) // 설정 파일 연결
@Log4j
public class ProductMapperTest {

	@Autowired
	private ProductMapper productMapper;
	
	@Test
	public void test() {
		testProductByProductId();
	}
	
	// 상품 상세 검색 테스트
	private void testProductByProductId() {
		ProductVO productVO = productMapper.selectProduct(1);
		log.info("testProductByProductId() : " + productVO);
	}
	
}
