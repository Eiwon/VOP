package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.web.vop.config.PaymentAPIConfig;
import com.web.vop.config.RootConfig;
import com.web.vop.config.S3Config;
import com.web.vop.config.SecurityConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.MemberVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, SecurityConfig.class, S3Config.class, PaymentAPIConfig.class, ServletConfig.class})
@Log4j
public class MemberMapperTest {

	
	@Autowired
	MemberMapper memberMapper;
	
	
	@Test
	public void test() {
		MemberVO memberVO = new MemberVO();
		memberVO.setMemberId("test1212");
		memberVO.setMemberPw("test1212");
		memberVO.setMemberName("test");
		memberVO.setMemberEmail("test@test.com");
		memberVO.setMemberPhone("01012341234");
		memberVO.setMemberAuth("�Ϲ�");
		insertMember(memberVO);
		selectByMemberId();
		selectMemberIdById();
		selectMemberIdWithPw();
//		updateMember();
//		updateMemberAuth();
//		selectAuthById();
//		selectByPhone();
//		updateMemberPw();
//		deleteMember();
//		selectByNameAndPhone();
//		selectIdByAuth();
//		selectIdByNameAndEmail();
//		selectEmailById();
//		selectIdByIdAndEmail();
//		revokeSellerAuth();
	}
	
	// ȸ�� ���
	public void insertMember(MemberVO memberVO) {
		log.info(memberMapper.insertMember(memberVO));
	} // end insertMember

	// memberId�� ȸ�� ��ȸ
	public void selectByMemberId() {
		log.info(memberMapper.selectByMemberId("test1234"));
	} // end selectByMemberId
	
	public void selectMemberIdById() {
		log.info(memberMapper.selectMemberIdById("test1234"));
	} // end selectMemberIdById
	
	public void selectMemberIdWithPw() {
		log.info(memberMapper.selectMemberIdWithPw("test1234", "test1234"));
	} // end selectMemberIdWithPw
	
	public void updateMember(){
		log.info(memberMapper.updateMember(new MemberVO()));
	} // end updateMember
	
	public void updateMemberAuth(){
		log.info(memberMapper.updateMemberAuth("test1234", "������"));
	} // end updateMemberAuth
	
	public void selectAuthById(){
		log.info(memberMapper.selectAuthById("test1234"));
	} // end selectAuthById
	
	public void selectByPhone(){
		log.info(memberMapper.selectByPhone("test1234"));
	} // end selectByPhone
	
	public void updateMemberPw(){
		log.info(memberMapper.updateMemberPw("test1234", "test1234"));
	} // end updateMemberPw
	
	public void deleteMember(){
		log.info(memberMapper.deleteMember("test1234"));
	} // end deleteMember
	
	public void selectByNameAndPhone(){
		log.info(memberMapper.selectByNameAndPhone("test", "01012345678"));
	} // end selectByNameAndPhone

	// �Է��� ������ ���� ��� ���� id �˻�
	public void selectIdByAuth(){
		log.info(memberMapper.selectIdByAuth("������"));
	} // end selectIdByAuth
	
	// �̸��Ϸ� ID ã��
	public void selectIdByNameAndEmail(){
		log.info(memberMapper.selectIdByNameAndEmail("test@test.com"));
	} // end selectIdByNameAndEmail
	
	// ID�� �̸��� ã��
	public void selectEmailById(){
		log.info(memberMapper.selectEmailById("test1234"));
	} // end selectEmailById
	
	// ID�� �̸��Ϸ� ID ã��
	public void selectIdByIdAndEmail(){
		log.info(memberMapper.selectIdByIdAndEmail("test1234", "test@test.com"));
	} // end selectIdByIdAndEmail
	
	// �Ǹ��� ���� ���(������ �Ϲ����� ����, ����� ��ϵ� ������� ��������� ����
	public void revokeSellerAuth(){
		log.info(memberMapper.revokeSellerAuth("test1234"));
	} // end revokeSellerAuth
	
}
