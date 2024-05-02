package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.domain.ReviewVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // ������ JUnit test ����
@ContextConfiguration(classes = {RootConfig.class}) // ���� ���� ����
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
		// �ڵ� ���� �ۼ� �ؾ���
		int result = reviewMapper.insertReview(reviewVO);
		log.info(result + "�� ����");
	} 
	
	private void testReviewList() {
		for(ReviewVO reviewVO : reviewMapper.selectListByReview(1)) {
			log.info(reviewVO);
		}
	}

	

}
