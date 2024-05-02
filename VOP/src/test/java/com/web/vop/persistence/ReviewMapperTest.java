package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.domain.ReviewVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 JUnit test 연결
@ContextConfiguration(classes = {RootConfig.class}) // 설정 파일 연결
@Log4j
public class ReviewMapperTest {
	
	@Autowired
	private ReviewMapper reviewMapper;
	
	@Test
	public void test() {
		testReviewInsert();
	}
	
	private void testReviewInsert() {
		ReviewVO vo = new ReviewVO();
		ReviewVO reviewVO = new ReviewVO();
		// 코드 마저 작성 해야함
		int result = reviewMapper.insertReview(reviewVO);
		log.info(result + "행 삽입");
	} 
	
	private void testReviewList() {
		for(ReviewVO reviewVO : reviewMapper.selectListByReview(1)) {
			log.info(reviewVO);
		}
	}

	

}
