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
		String requestState = "승인 대기중";
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
	
	// 판매자 권한 요청 목록 검색
    public void selectRequestByStateTest(String requestState, Pagination pagination) {
        log.info(sellerMapper.selectRequestByState(requestState, pagination));
    } // end selectRequestByStateTest

    // 판매자 권한 요청 목록 수 검색
    public void selectRequestByStateCntTest(String requestState) {
        log.info(sellerMapper.selectRequestByStateCnt(requestState));
    } // end selectRequestByStateCntTest

    // 판매자 권한 요청 상세정보 조회
    public void selectRequestByIdTest(String memberId) {
        log.info(sellerMapper.selectRequestById(memberId));
    } // end selectRequestByIdTest

    // 판매자 권한 요청 등록
    public void insertRequestTest(SellerVO sellerVO) {
        log.info(sellerMapper.insertRequest(sellerVO));
    } // end insertRequestTest

    // 판매자 권한 요청 내용 수정 (회원)
    public void updateMemberContentTest(SellerVO sellerVO) {
        log.info(sellerMapper.updateMemberContent(sellerVO));
    } // end updateMemberContentTest

    // 판매자 권한 요청 내용 수정 (관리자)
    public void updateAdminContentTest(SellerVO sellerVO) {
        log.info(sellerMapper.updateAdminContent(sellerVO));
    } // end updateAdminContentTest

    // 판매자 권한 요청 삭제
    public void deleteRequestTest(String memberId) {
        log.info(sellerMapper.deleteRequest(memberId));
    } // end deleteRequestTest

    // 판매자 권한 요청 상세정보 조회
    public void selectSellerRequestDetailsTest(String memberId) {
        log.info(sellerMapper.selectSellerRequestDetails(memberId));
    } // end selectSellerRequestDetailsTest
}
