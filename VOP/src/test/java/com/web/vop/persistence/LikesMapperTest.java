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
import com.web.vop.domain.LikesVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class LikesMapperTest {
	
	@Autowired
	private LikesMapper likesMapper;
	 
	@Test
	public void test() {
		LikesVO likesVO = new LikesVO();
        int productId = 27;
        int reviewId = 85;
        String memberId = "test1234";
        likesVO.setLikesId(300);
        likesVO.setLikesType(0);
        likesVO.setMemberId(memberId);
        likesVO.setProductId(productId);
        likesVO.setReviewId(reviewId);

        //insertLikesTest(likesVO);
        selectByLikesPagingTest(productId, memberId);
        selectByLikesTest(reviewId, memberId);
        //updateLikesTest(likesVO);
        //deleteLikesTest(reviewId, memberId);
	}
	
	// ���ƿ� or �Ⱦ�� ���
    public void insertLikesTest(LikesVO likesVO) {
        log.info(likesMapper.insertLikes(likesVO));
    } // end insertLikesTest

    // ���ƿ� or �Ⱦ�� ����¡ �˻�
    public void selectByLikesPagingTest(int productId, String memberId) {
        log.info(likesMapper.selectByLikesPaging(productId, memberId));
    } // end selectByLikesPagingTest

    // ���ƿ� or �Ⱦ�� �˻�
    public void selectByLikesTest(int reviewId, String memberId) {
        log.info(likesMapper.selectByLikes(reviewId, memberId));
    } // end selectByLikesTest

    // ���ƿ� or �Ⱦ�� ����
    public void updateLikesTest(LikesVO likesVO) {
        log.info(likesMapper.updateLikes(likesVO));
    } // end updateLikesTest

    // ���ƿ� or �Ⱦ�� ����
    public void deleteLikesTest(int reviewId, String memberId) {
        log.info(likesMapper.deleteLikes(reviewId, memberId));
    } // end deleteLikesTest
}
