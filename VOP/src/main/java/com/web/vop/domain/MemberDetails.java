package com.web.vop.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private MemberVO memberVO;
	private List<Integer> blockPopupList;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 해당 유저의 권한들 return
		
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new GrantedAuthority() {
			private static final long serialVersionUID = 2L;

			@Override
			public String getAuthority() {
				return "ROLE_" + memberVO.getMemberAuth();
			}
		});
		
		return auths;
	} // end getAuthorities

	@Override
	public String getPassword() {
		return memberVO.getMemberPw();
	}

	@Override
	public String getUsername() {
		return memberVO.getMemberId();
	}

	public String getAuth() {
		return memberVO.getMemberAuth();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
