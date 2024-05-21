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
	// 로그인시, security filter가 가로채서 호출하는 서비스

	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("security : " + username);
		MemberVO memberVO = memberMapper.selectByMemberId(username);
		// ID로 계정 정보 검색 후, 검색한 비밀번호와 입력된 비밀번호를 비교 
		// (비밀번호는 암호화되서 저장하기 때문에 가져와서 복호화한 후 비교)
		
		if(memberVO == null) {
			log.info("잘못된 ID 또는 비밀번호");
			return null;
		}
		MemberDetails memberDetails = new MemberDetails();
		memberDetails.setMemberVO(memberVO);
		
		return memberDetails;
	} // end loadUserByUsername

}
