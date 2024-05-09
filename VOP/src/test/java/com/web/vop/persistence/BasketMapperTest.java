package com.web.vop.persistence;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.domain.BasketDTO;
import com.web.vop.domain.BasketVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 JUnit test 연결
@ContextConfiguration(classes = {RootConfig.class}) // 설정 파일 연결
@Log4j
public class BasketMapperTest {

	@Autowired
	BasketMapper basketMapper;
	
	@Test
	public void test() {
		//getMyBasket();
		//getMyBasketCnt();
		insertTest();
	}
	
	private void getMyBasket() {
		List<BasketDTO> list = basketMapper.selectByMemberId("test1234");
		log.info(list);
		
	} // end getMyBasket
	
	private void getMyBasketCnt() {
		int count = basketMapper.selectByMemberIdCnt("test1234");
	} // end getMyBasketCnt
	
	private void insertTest() {
		int res = basketMapper.insertToBasket(new BasketVO("test1234", 15, 50));
	} // end insertTest
	
}
