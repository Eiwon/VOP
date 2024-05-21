package com.web.vop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.vop.domain.MemberDetails;
import com.web.vop.domain.MemberVO;
import com.web.vop.persistence.MemberMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UserDetailsServiceImple implements UserDetailsService{
	// �α��ν�, security filter�� ����ä�� ȣ���ϴ� ����

	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("security : " + username);
		MemberVO memberVO = memberMapper.selectByMemberId(username);
		// ID�� ���� ���� �˻� ��, �˻��� ��й�ȣ�� �Էµ� ��й�ȣ�� �� 
		// (��й�ȣ�� ��ȣȭ�Ǽ� �����ϱ� ������ �����ͼ� ��ȣȭ�� �� ��)
		
		if(memberVO == null) {
			log.info("�߸��� ID �Ǵ� ��й�ȣ");
			return null;
		}
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberVO(memberVO);
		
		return memberDetails;
	} // end loadUserByUsername

}
