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
	
	// ���(����) ���
    public void insertInquiryTest(InquiryVO inquiryVO) {
        log.info(inquiryMapper.insertInquiry(inquiryVO));
    } // end insertInquiryTest

    // ���(����) ��ü �˻�
    public void selectListByInquiryTest(int productId) {
        log.info(inquiryMapper.selectListByInquiry(productId));
    } // end selectListByInquiryTest

    // memberId�� ���� �˻� ����¡
    public void selectListByInquiryMemberIdPagingNewTest(String memberId, Pagination pagination) {
        log.info(inquiryMapper.selectListByInquiryMemberIdPagingNew(memberId, pagination));
    } // end selectListByInquiryMemberIdPagingNewTest

    // memberId�� ���� �˻� ����¡
    public void selectListByInquiryMemberIdPagingTest(String memberId, Pagination pagination) {
        log.info(inquiryMapper.selectListByInquiryMemberIdPaging(memberId, pagination));
    } // end selectListByInquiryMemberIdPagingTest

    // ���(����) memberId�� ����¡ �� �˻� �뵵
    public void selectListByInquiryMemberIdCntTest(String memberId) {
        log.info(inquiryMapper.selectListByInquiryMemberIdCnt(memberId));
    } // end selectListByInquiryMemberIdCntTest

    // productId�� ���(����) ��ü �˻�
    public void selectListByInquiryPagingTest(int productId, Pagination pagination) {
        log.info(inquiryMapper.selectListByInquiryPaging(productId, pagination));
    } // end selectListByInquiryPagingTest

    // ���(����) productId�� ����¡ �� �˻� �뵵
    public void selectListByInquiryCntTest(int productId) {
        log.info(inquiryMapper.selectListByInquiryCnt(productId));
    } // end selectListByInquiryCntTest

    // ���(����) productId �׸��� memberId ���� �˻�
    public void selectByInquiryTest(int productId, String memberId) {
        log.info(inquiryMapper.selectByInquiry(productId, memberId));
    } // end selectByInquiryTest

    // ���(����) ����
    public void updateInquiryTest(InquiryVO inquiryVO) {
        log.info(inquiryMapper.updateInquiry(inquiryVO));
    } // end updateInquiryTest

    // ���(����) ����
    public void deleteInquiryTest(int productId, String memberId) {
        log.info(inquiryMapper.deleteInquiry(productId, memberId));
    } // end deleteInquiryTest
}
