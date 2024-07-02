package com.web.vop.persistence;

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
import com.web.vop.config.WebSocketConfig;
import com.web.vop.domain.MemberVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class MemberMapperTest {

	@Rule
	public JUnitRestDocumentation restDocs = new JUnitRestDocumentation();
    
	private MockMvc mockMvc;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	 @Before
	 public void setUp() {
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
	                                   .apply(MockMvcRestDocumentation.documentationConfiguration(restDocs))
	                                   .build();
	 }
	 
	
	
	@Test
	public void test() {
		MemberVO memberVO = new MemberVO();
		memberVO.setMemberId("test1212");
		memberVO.setMemberPw("test1212");
		memberVO.setMemberName("test");
		memberVO.setMemberEmail("test@test.com");
		memberVO.setMemberPhone("01011111111");
		memberVO.setMemberAuth("�Ϲ�");
		String memberId = memberVO.getMemberId();
		String memberPw = memberVO.getMemberPw();
//		insertMember(memberVO);
//		selectByMemberId(memberVO.getMemberId());
//		selectMemberIdById(memberVO.getMemberId());
//		selectMemberIdWithPw(memberVO.getMemberId(), memberVO.getMemberPw());
//		updateMember(memberVO);
//		updateMemberAuth(memberVO.getMemberId(), memberVO.getMemberAuth());
//		selectAuthById(memberVO.getMemberId());
//		selectByPhone(memberVO.getMemberPhone());
//		updateMemberPw(memberId, memberPw);
//		deleteMember(memberId);
//		selectByNameAndPhone(memberVO.getMemberName(), memberVO.getMemberEmail());
//		selectIdByAuth("�Ϲ�");
//		selectIdByNameAndEmail("test12@test.com");
//		selectEmailById();
//		selectIdByIdAndEmail();
		revokeSellerAuth();
	}
	
	// ȸ�� ���
	public void insertMember(MemberVO memberVO) {
		log.info(memberMapper.insertMember(memberVO));
	} // end insertMember

	// memberId�� ȸ�� ��ȸ
	public void selectByMemberId(String memberId) {
		log.info(memberMapper.selectByMemberId(memberId));
	} // end selectByMemberId
	
	public void selectMemberIdById(String memberId) {
		log.info(memberMapper.selectMemberIdById(memberId));
	} // end selectMemberIdById
	
	public void selectMemberIdWithPw(String memberId, String memberPw) {
		log.info(memberMapper.selectMemberIdWithPw(memberId, memberPw));
	} // end selectMemberIdWithPw
	
	public void updateMember(MemberVO memberVO){
		log.info(memberMapper.updateMember(memberVO));
	} // end updateMember
	
	public void updateMemberAuth(String memberId, String memberAuth){
		log.info(memberMapper.updateMemberAuth(memberId, memberAuth));
	} // end updateMemberAuth
	
	public void selectAuthById(String memberId){
		log.info(memberMapper.selectAuthById(memberId));
	} // end selectAuthById
	
	public void selectByPhone(String memberPhone){
		log.info(memberMapper.selectByPhone(memberPhone));
	} // end selectByPhone
	
	public void updateMemberPw(String memberId, String memberPw){
		log.info(memberMapper.updateMemberPw(memberId, memberPw));
	} // end updateMemberPw
	
	public void deleteMember(String memberId){
		log.info(memberMapper.deleteMember(memberId));
	} // end deleteMember
	
	public void selectByNameAndPhone(String memberName, String memberPhone){
		log.info(memberMapper.selectByNameAndPhone(memberName, memberName));
	} // end selectByNameAndPhone

	// �Է��� ������ ���� ��� ���� id �˻�
	public void selectIdByAuth(String memberAuth){
		log.info(memberMapper.selectIdByAuth(memberAuth));
	} // end selectIdByAuth
	
	// �̸��Ϸ� ID ã��
	public void selectIdByNameAndEmail(String memberEmail){
		log.info(memberMapper.selectIdByNameAndEmail(memberEmail));
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
