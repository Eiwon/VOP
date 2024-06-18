package com.web.vop.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenAuthenticationServiceImple implements TokenAuthenticationService{

	private String signingKey = "testSignKey";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String REFRESH_HEADER = "Refresh";
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
				.setIssuedAt(new Date())
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

	@Override
	public String createAccessToken(UserDetails memberDetails) {
		String accessToken = Jwts.builder()
				 .setSubject(memberDetails.getUsername())
				 .setIssuedAt(new Date())
				 .setExpiration(new Date(System.currentTimeMillis() + (5*60*1000)))
				 .signWith(SignatureAlgorithm.HS512, signingKey)
				 .compact();
		return accessToken;
	}

	@Override
	public String createRefreshToken(UserDetails memberDetails) {
		String refreshToken = Jwts.builder()
				  .setSubject(memberDetails.getUsername())
				  .setIssuedAt(new Date())
				  .setExpiration(new Date(System.currentTimeMillis() + (10*60*1000)))
				  .compact();
		return refreshToken;
	}

	@Override
	public boolean isValidToken(String token, HttpServletResponse response) {
		
		return false;
	}

	@Override
	public String extractAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		String token = null;
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
		}
		
		return token;
	}

	@Override
	public String extractRefreshToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(REFRESH_HEADER);
		String token = null;
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
		}
		
		return token;
	}

}
