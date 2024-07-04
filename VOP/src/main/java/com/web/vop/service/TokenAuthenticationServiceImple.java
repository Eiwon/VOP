
package com.web.vop.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j;

@Log4j
public class TokenAuthenticationServiceImple implements TokenAuthenticationService{

	private String signingKey = "testSignKey";
	private static final String ACCESS_TOKEN_HEADER = "access_token";
	private static final String REFRESH_TOKEN_HEADER = "refresh_token";
	@Autowired
	private UserDetailsService UserDetailsService;
	
	@Override
	public String getUsernameFromToken(String token) throws ExpiredJwtException{
		
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
				.signWith(SignatureAlgorithm.HS256, signingKey)
				.compact();
	}

	@Override
	public UserDetails getUserFromToken(String token) throws ExpiredJwtException{
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
				 .claim("role", memberDetails.getAuthorities())
				 .signWith(SignatureAlgorithm.HS256, signingKey)
				 .compact();
		return accessToken;
	}

	@Override
	public String createRefreshToken(UserDetails memberDetails) {
		String refreshToken = Jwts.builder()
				  .setSubject(memberDetails.getUsername())
				  .setIssuedAt(new Date())
				  .setExpiration(new Date(System.currentTimeMillis() + (60*60*24*7*1000)))
				  .compact();
		return refreshToken;
	}

	@Override
	public boolean isValidToken(String token) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
		Date now = new Date();
		
		if(claims.getBody().getExpiration().after(now)) {
			return true;
		}
		return false;
	}

	@Override
	public String extractAccessToken(HttpServletRequest request) {
		String bearerToken = null;
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(ACCESS_TOKEN_HEADER)) {
					try {
						bearerToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		String token = null;
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
		}
		
		return token;
	}

	@Override
	public String extractRefreshToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(REFRESH_TOKEN_HEADER);
		String token = null;
		
		if(!StringUtils.hasText(bearerToken)) {
			return null;
		}
		
		try {
			bearerToken = URLDecoder.decode(bearerToken, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			token = bearerToken.substring(7);
		}
		
		return token;
	}

	@Override
	public String getAuthFromToken(String token) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
		String role = (String)claims.getBody().get("role");
		log.info("토큰에서 권한 추출 : " + role);
		return role;
	}

}
