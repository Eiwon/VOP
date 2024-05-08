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

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 JUnit test 연결
@ContextConfiguration(classes = {RootConfig.class}) // 설정 파일 연결
@Log4j
public class ProductMapperTest {

	@Autowired
	private ProductMapper productMapper;
	
	@Test
	public void test() {
//		testProductByProductId(); // 테스트 완료
//		testSelectReviewByCount(); // 테스트 완료
		testSelectReviewByStar(); // 테스트 완료
//		testSelectByMainImg(); // 없어도 되는 코드
	}
	
	// 상품 상세 검색 테스트
	private void testProductByProductId() {
		ProductVO productVO = productMapper.selectProduct(1);
		log.info("testProductByProductId() : " + productVO);
	}
	
	// 댓글 총 갯수 조회 테스트
	private void testSelectReviewByCount() {
		int res = productMapper.selectReviewCount(4);
		log.info("testSelectReviewByCount() : " + res);
	}
	
	// 리뷰 총 합
	private void testSelectReviewByStar() {
		int res = productMapper.selectReviewStar(2);
		log.info("testSelectReviewByStar() : " + res);
	}
	
//	// 이미지 상세 검색 테스트
//	private void testSelectByMainImg() {
//		ProductVO ProductVO = productMapper.selectByMainImg(2);
//		log.info("testImageByImageId()" + ProductVO);
//	}
	
}
