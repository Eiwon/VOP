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
import com.web.vop.domain.AnswerVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class AnswerMapperTest {

	@Autowired
	private AnswerMapper answerMapper;
	 
	@Test
	public void test() {
		AnswerVO answerVO = new AnswerVO();
		answerVO.setAnswerId(1);
		answerVO.setInquiryId(120);
		answerVO.setMemberId("test1234");
		answerVO.setProductId(27);
		answerVO.setAnswerContent("aaaa");
		
//		insertAnswerTest(answerVO);
//        selectListByProductIdTest(answerVO.getProductId());
        selectListByInquiryIdTest(answerVO.getInquiryId());
        updateAnswerTest(answerVO);
        deleteAnswerTest(answerVO.getInquiryId(), answerVO.getMemberId());
	}
	
	// ´ñ´ñ±Û(´äº¯) µî·Ï
    public void insertAnswerTest(AnswerVO answerVO) {
        log.info(answerMapper.insertAnswer(answerVO));
    } // end insertAnswerTest

    // ´ñ´ñ±Û(´äº¯) ÀüÃ¼ °Ë»ö by ProductId
    public void selectListByProductIdTest(int productId) {
        log.info(answerMapper.selectListByProductId(productId));
    } // end selectListByProductIdTest

    // ´ñ´ñ±Û(´äº¯) ÀüÃ¼ °Ë»ö by InquiryId
    public void selectListByInquiryIdTest(int inquiryId) {
        log.info(answerMapper.selectListByInquiryId(inquiryId));
    } // end selectListByInquiryIdTest

    // ´ñ´ñ±Û(´äº¯) ¼öÁ¤
    public void updateAnswerTest(AnswerVO answerVO) {
        log.info(answerMapper.updateAnswer(answerVO));
    } // end updateAnswerTest

    // ´ñ´ñ±Û(´äº¯) »èÁ¦
    public void deleteAnswerTest(int inquiryId, String memberId) {
        log.info(answerMapper.deleteAnswer(inquiryId, memberId));
    } // end deleteAnswerTest
}
