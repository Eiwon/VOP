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
import com.web.vop.domain.InquiryVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class InquiryMapperTest {

	@Autowired
	private InquiryMapper inquiryMapper;
	 
	@Test
	public void test() {
		InquiryVO inquiryVO = new InquiryVO();
        int productId = 27;
        String memberId = "test1234";
        Pagination pagination = new Pagination();
        inquiryVO.setInquiryContent("123");
        inquiryVO.setInquiryId(50);
        inquiryVO.setMemberId(memberId);
        inquiryVO.setProductId(productId);
        
//        insertInquiryTest(inquiryVO);
//        selectListByInquiryTest(productId);
//        selectListByInquiryMemberIdPagingNewTest(memberId, pagination);
//        selectListByInquiryMemberIdPagingTest(memberId, pagination);
//        selectListByInquiryMemberIdCntTest(memberId);
//        selectListByInquiryPagingTest(productId, pagination);
//        selectListByInquiryCntTest(productId);
//        selectByInquiryTest(productId, memberId);
        updateInquiryTest(inquiryVO);
        deleteInquiryTest(productId, memberId);
        
	}
	
	// 댓글(문의) 등록
    public void insertInquiryTest(InquiryVO inquiryVO) {
        log.info(inquiryMapper.insertInquiry(inquiryVO));
    } // end insertInquiryTest

    // 댓글(문의) 전체 검색
    public void selectListByInquiryTest(int productId) {
        log.info(inquiryMapper.selectListByInquiry(productId));
    } // end selectListByInquiryTest

    // memberId로 문의 검색 페이징
    public void selectListByInquiryMemberIdPagingNewTest(String memberId, Pagination pagination) {
        log.info(inquiryMapper.selectListByInquiryMemberIdPagingNew(memberId, pagination));
    } // end selectListByInquiryMemberIdPagingNewTest

    // memberId로 문의 검색 페이징
    public void selectListByInquiryMemberIdPagingTest(String memberId, Pagination pagination) {
        log.info(inquiryMapper.selectListByInquiryMemberIdPaging(memberId, pagination));
    } // end selectListByInquiryMemberIdPagingTest

    // 댓글(문의) memberId로 페이징 수 검색 용도
    public void selectListByInquiryMemberIdCntTest(String memberId) {
        log.info(inquiryMapper.selectListByInquiryMemberIdCnt(memberId));
    } // end selectListByInquiryMemberIdCntTest

    // productId로 댓글(문의) 전체 검색
    public void selectListByInquiryPagingTest(int productId, Pagination pagination) {
        log.info(inquiryMapper.selectListByInquiryPaging(productId, pagination));
    } // end selectListByInquiryPagingTest

    // 댓글(문의) productId로 페이징 수 검색 용도
    public void selectListByInquiryCntTest(int productId) {
        log.info(inquiryMapper.selectListByInquiryCnt(productId));
    } // end selectListByInquiryCntTest

    // 댓글(문의) productId 그리고 memberId 통해 검색
    public void selectByInquiryTest(int productId, String memberId) {
        log.info(inquiryMapper.selectByInquiry(productId, memberId));
    } // end selectByInquiryTest

    // 댓글(문의) 수정
    public void updateInquiryTest(InquiryVO inquiryVO) {
        log.info(inquiryMapper.updateInquiry(inquiryVO));
    } // end updateInquiryTest

    // 댓글(문의) 삭제
    public void deleteInquiryTest(int productId, String memberId) {
        log.info(inquiryMapper.deleteInquiry(productId, memberId));
    } // end deleteInquiryTest
}
