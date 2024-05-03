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
//		testReviewList(); // �׽�Ʈ �Ϸ�
		testReviewInsert(); // �׽�Ʈ �Ϸ�
//		testReviewUpdate(); // �׽�Ʈ �Ϸ�
//		testBoarDelete(); // �׽�Ʈ �Ϸ� 
	}
	
	// ���(����) ��� �׽�Ʈ
	private void testReviewInsert() {
		// �ۼ� ��
		ReviewVO vo = new ReviewVO(35, "test", 2, "This", null, 3.5f, 10);
		int result = reviewMapper.insertReview(vo);
		log.info(result + "�� ����");
	} 
	
	// ���(����) ��ü �˻�
	private void testReviewList() {
		for(ReviewVO reviewVO : reviewMapper.selectListByReview(4)) {
			log.info(reviewVO);
		}
	}
	
	// ���(����) ����
	private void testReviewUpdate() {
		ReviewVO reviewVO = new ReviewVO();
		reviewVO.setReviewId(17);
		reviewVO.setReviewContent("test");
		reviewVO.setReviewStar(3.1f);
		int result = reviewMapper.updateReview(reviewVO);
		log.info(result + "�� ����");
	}
	
	// ���(����) ����
	private void testBoarDelete() {
		int result = reviewMapper.deleteReview(17);
		log.info(result + "�� ����");
	}
	

	

}
