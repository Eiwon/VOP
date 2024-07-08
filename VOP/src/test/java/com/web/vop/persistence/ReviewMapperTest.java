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
	

    // ´ñ±Û(¸®ºä) µî·Ï
    public void insertReviewTest(ReviewVO reviewVO) {
        log.info(reviewMapper.insertReview(reviewVO));
    } // end insertReviewTest

    // ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö
    public void selectListByReviewTest(int productId) {
        log.info(reviewMapper.selectListByReview(productId));
    } // end selectListByReviewTest

    // ´ñ±Û(¸®ºä) ÀüÃ¼ °Ë»ö ÆäÀÌÂ¡ Ã³¸®
    public void selectListByReviewPagingTest(int productId, Pagination pagination) {
        log.info(reviewMapper.selectListByReviewPaging(productId, pagination));
    } // end selectListByReviewPagingTest

    // ´ñ±Û(¸®ºä) ÆäÀÌÂ¡ Ã³¸® ¼ö °Ë»ö
    public void selectListByReviewCntTest(int productId) {
        log.info(reviewMapper.selectListByReviewCnt(productId));
    } // end selectListByReviewCntTest

    // ´ñ±Û(¸®ºä) È¸¿øID·Î ÀüÃ¼ °Ë»ö
    public void selectListByReviewMemberIdTest(String memberId) {
        log.info(reviewMapper.selectListByReviewMemberId(memberId));
    } // end selectListByReviewMemberIdTest

    // ´ñ±Û(¸®ºä) productId ±×¸®°í memberId ÅëÇØ °Ë»ö
    public void selectByReviewTest(int productId, String memberId) {
        log.info(reviewMapper.selectByReview(productId, memberId));
    } // end selectByReviewTest

    // ´ñ±Û(¸®ºä) ¼öÁ¤
    public void updateReviewTest(ReviewVO reviewVO) {
        log.info(reviewMapper.updateReview(reviewVO));
    } // end updateReviewTest

    // ´ñ±Û(¸®ºä) »èÁ¦
    public void deleteReviewTest(int productId, String memberId) {
        log.info(reviewMapper.deleteReview(productId, memberId));
    } // end deleteReviewTest

    // ´ñ±Û ÁÁ¾Æ¿ä UP
    public void updateReviewLikeUpTest(int reviewId) {
        log.info(reviewMapper.updateReviewLikeUp(reviewId));
    } // end updateReviewLikeUpTest

    // ´ñ±Û ÁÁ¾Æ¿ä DOWN
    public void updateReviewLikeDownTest(int reviewId) {
        log.info(reviewMapper.updateReviewLikeDown(reviewId));
    } // end updateReviewLikeDownTest

    // »óÇ° ¸®ºä(º°) ÃÑÇÕ °Ë»ö
    public void selectReviewStarTest(int productId) {
        log.info(reviewMapper.selectReviewStar(productId));
    } // end selectReviewStarTest

    // »óÇ°ÀÇ ¸®ºä Æò±Õ°ª °Ë»ö
    public void selectReviewAgvTest(int productId) {
        log.info(reviewMapper.selectReviewAgv(productId));
    } // end selectReviewAgvTest
}
