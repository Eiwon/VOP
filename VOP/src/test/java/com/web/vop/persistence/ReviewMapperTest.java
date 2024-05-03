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
//		testReviewList(); // 테스트 완료
		testReviewInsert(); // 테스트 완료
//		testReviewUpdate(); // 테스트 완료
//		testBoarDelete(); // 테스트 완료 
	}
	
	// 댓글(리뷰) 등록 테스트
	private void testReviewInsert() {
		// 작성 중
		ReviewVO vo = new ReviewVO(35, "test", 2, "This", null, 3.5f, 10);
		int result = reviewMapper.insertReview(vo);
		log.info(result + "행 삽입");
	} 
	
	// 댓글(리뷰) 전체 검색
	private void testReviewList() {
		for(ReviewVO reviewVO : reviewMapper.selectListByReview(4)) {
			log.info(reviewVO);
		}
	}
	
	// 댓글(리뷰) 수정
	private void testReviewUpdate() {
		ReviewVO reviewVO = new ReviewVO();
		reviewVO.setReviewId(17);
		reviewVO.setReviewContent("test");
		reviewVO.setReviewStar(3.1f);
		int result = reviewMapper.updateReview(reviewVO);
		log.info(result + "행 수정");
	}
	
	// 댓글(리뷰) 삭제
	private void testBoarDelete() {
		int result = reviewMapper.deleteReview(17);
		log.info(result + "행 삭제");
	}
	

	

}
