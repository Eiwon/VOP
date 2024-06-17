package com.web.vop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenAuthenticationServiceImple implements TokenAuthenticationService{

	private String signingKey = "testSignKey";
	
	@Autowired
	private UserDetailsService UserDetailsService;
	
	@Override
	public String getUsernameFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(signingKey) 
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	@Override
	public String createToken(UserDetails memberDetails) {
		return Jwts.builder()
				.setSubject(memberDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + (30*60*1000)))
				.signWith(SignatureAlgorithm.HS512, signingKey)
				.compact();
	}

	@Override
	public UserDetails getUserFromToken(String token) {
		String memberName = getUsernameFromToken(token);
		UserDetails memberDetails = UserDetailsService.loadUserByUsername(memberName);
		return memberDetails;
	}

}
