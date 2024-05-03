package com.web.vop.persistence;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.SellerVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class})
@Log4j
public class SellerMapperTest {

	@Autowired
	SellerMapper sellerMapper; 
	
	@Test
	public void test() {
		//insertTest();
		//selectTest();
		//getCountTest();
		selectById();
	}
	
	public void insertTest() {
		SellerVO vo = new SellerVO(
				"test1234", "soldesk", null, "test", null, null);
		int res = sellerMapper.insertRequest(vo);
		log.info(res + "행 추가 성공");
	} // end insertTest
	
	public void selectTest() {
		List<SellerVO> list = sellerMapper.selectAllRequest(new Pagination());
		log.info(list);
	} // end selectTest
	
	public void getCountTest() {
		int res = sellerMapper.selectRequestCount();
		log.info("결과 : " + res);
	} // end getCountTest
	
	public void selectById() {
		SellerVO vo = sellerMapper.selectRequestById("test1234");
		log.info("결과 : " + vo);
	}
}




