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
import com.web.vop.domain.ReviewVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class ReviewMapperTest {

	@Autowired
	private ReviewMapper reviewMapper;
	 
	@Test
	public void test() {
		ReviewVO reviewVO = new ReviewVO();
        int productId = 27;
        String memberId = "test1234";
        Pagination pagination = new Pagination();
        int reviewId = 1000;
        reviewVO.setMemberId(memberId);
        reviewVO.setProductId(productId);
        reviewVO.setReviewContent("123");
        reviewVO.setReviewId(reviewId);
        reviewVO.setReviewLike(0);
        reviewVO.setReviewStar(5);

//        insertReviewTest(reviewVO);
        selectListByReviewTest(productId);
        selectListByReviewPagingTest(productId, pagination);
        selectListByReviewCntTest(productId);
        selectListByReviewMemberIdTest(memberId);
        selectByReviewTest(productId, memberId);
//        updateReviewTest(reviewVO);
//        deleteReviewTest(productId, memberId);
//        updateReviewLikeUpTest(reviewId);
        updateReviewLikeDownTest(reviewId);
        selectReviewStarTest(productId);
        selectReviewAgvTest(productId);
	}
	

    // ���(����) ���
    public void insertReviewTest(ReviewVO reviewVO) {
        log.info(reviewMapper.insertReview(reviewVO));
    } // end insertReviewTest

    // ���(����) ��ü �˻�
    public void selectListByReviewTest(int productId) {
        log.info(reviewMapper.selectListByReview(productId));
    } // end selectListByReviewTest

    // ���(����) ��ü �˻� ����¡ ó��
    public void selectListByReviewPagingTest(int productId, Pagination pagination) {
        log.info(reviewMapper.selectListByReviewPaging(productId, pagination));
    } // end selectListByReviewPagingTest

    // ���(����) ����¡ ó�� �� �˻�
    public void selectListByReviewCntTest(int productId) {
        log.info(reviewMapper.selectListByReviewCnt(productId));
    } // end selectListByReviewCntTest

    // ���(����) ȸ��ID�� ��ü �˻�
    public void selectListByReviewMemberIdTest(String memberId) {
        log.info(reviewMapper.selectListByReviewMemberId(memberId));
    } // end selectListByReviewMemberIdTest

    // ���(����) productId �׸��� memberId ���� �˻�
    public void selectByReviewTest(int productId, String memberId) {
        log.info(reviewMapper.selectByReview(productId, memberId));
    } // end selectByReviewTest

    // ���(����) ����
    public void updateReviewTest(ReviewVO reviewVO) {
        log.info(reviewMapper.updateReview(reviewVO));
    } // end updateReviewTest

    // ���(����) ����
    public void deleteReviewTest(int productId, String memberId) {
        log.info(reviewMapper.deleteReview(productId, memberId));
    } // end deleteReviewTest

    // ��� ���ƿ� UP
    public void updateReviewLikeUpTest(int reviewId) {
        log.info(reviewMapper.updateReviewLikeUp(reviewId));
    } // end updateReviewLikeUpTest

    // ��� ���ƿ� DOWN
    public void updateReviewLikeDownTest(int reviewId) {
        log.info(reviewMapper.updateReviewLikeDown(reviewId));
    } // end updateReviewLikeDownTest

    // ��ǰ ����(��) ���� �˻�
    public void selectReviewStarTest(int productId) {
        log.info(reviewMapper.selectReviewStar(productId));
    } // end selectReviewStarTest

    // ��ǰ�� ���� ��հ� �˻�
    public void selectReviewAgvTest(int productId) {
        log.info(reviewMapper.selectReviewAgv(productId));
    } // end selectReviewAgvTest
}
