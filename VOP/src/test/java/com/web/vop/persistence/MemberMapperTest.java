package com.web.vop.persistence;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.MemberVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class})
@Log4j
public class MemberMapperTest {
	
	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	DataSource dataSource; 
	
	@Test
	public void test() {
		testMemberInsert();
	}
	
	private void testConnection() {
		try (Connection conn = dataSource.getConnection()){
			log.info("Connection ∞¥√º : " + conn);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	// insert member test
	private void testMemberInsert() {
		MemberVO memberVo = new MemberVO("test", "test", "test", "test", "test", "¿œπ›");
		int result = memberMapper.insertMember(memberVo);
		log.info("testMemberInsert() : " + result);
	} // end testMemberInsert
	
	private void testSelectByMemberId() {
		String result = memberMapper.selectByPhone("asd");
		log.info("testSelectByMemberId() ");
	}
}
