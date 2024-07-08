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
import com.web.vop.domain.SellerVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class SellerMapperTest {
	
	@Autowired
	private SellerMapper sellerMapper;
	 
	@Test
	public void test() {
		String requestState = "���� �����";
        Pagination pagination = new Pagination();
        String memberId = "test123456";
        SellerVO sellerVO = new SellerVO();
        sellerVO.setBusinessName("test");
        sellerVO.setMemberId(memberId);
        sellerVO.setRequestContent("test");
        sellerVO.setRequestState(requestState);
        sellerVO.setRefuseMsg("1234");
        
//        selectRequestByStateTest(requestState, pagination);
//        selectRequestByStateCntTest(requestState);
//        selectRequestByIdTest(memberId);
//        insertRequestTest(sellerVO);
        updateMemberContentTest(sellerVO);
        updateAdminContentTest(sellerVO);
        deleteRequestTest(memberId);
//        selectSellerRequestDetailsTest(memberId);
	}
	
	// �Ǹ��� ���� ��û ��� �˻�
    public void selectRequestByStateTest(String requestState, Pagination pagination) {
        log.info(sellerMapper.selectRequestByState(requestState, pagination));
    } // end selectRequestByStateTest

    // �Ǹ��� ���� ��û ��� �� �˻�
    public void selectRequestByStateCntTest(String requestState) {
        log.info(sellerMapper.selectRequestByStateCnt(requestState));
    } // end selectRequestByStateCntTest

    // �Ǹ��� ���� ��û ������ ��ȸ
    public void selectRequestByIdTest(String memberId) {
        log.info(sellerMapper.selectRequestById(memberId));
    } // end selectRequestByIdTest

    // �Ǹ��� ���� ��û ���
    public void insertRequestTest(SellerVO sellerVO) {
        log.info(sellerMapper.insertRequest(sellerVO));
    } // end insertRequestTest

    // �Ǹ��� ���� ��û ���� ���� (ȸ��)
    public void updateMemberContentTest(SellerVO sellerVO) {
        log.info(sellerMapper.updateMemberContent(sellerVO));
    } // end updateMemberContentTest

    // �Ǹ��� ���� ��û ���� ���� (������)
    public void updateAdminContentTest(SellerVO sellerVO) {
        log.info(sellerMapper.updateAdminContent(sellerVO));
    } // end updateAdminContentTest

    // �Ǹ��� ���� ��û ����
    public void deleteRequestTest(String memberId) {
        log.info(sellerMapper.deleteRequest(memberId));
    } // end deleteRequestTest

    // �Ǹ��� ���� ��û ������ ��ȸ
    public void selectSellerRequestDetailsTest(String memberId) {
        log.info(sellerMapper.selectSellerRequestDetails(memberId));
    } // end selectSellerRequestDetailsTest
}
