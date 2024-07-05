
package com.web.vop.config;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.amazonaws.services.guardduty.model.SecurityContext;
import com.web.vop.service.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j;
@Log4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = jwtTokenProvider.extractAccessToken(request);
		try {
		if(StringUtils.hasText(token)) {
			UserDetails memberDetails = jwtTokenProvider.getUserFromToken(token);
			if(memberDetails != null) {
				log.info("UserDetails name : " + memberDetails.getUsername());
				UsernamePasswordAuthenticationToken authenticationToken
					= new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				log.info(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
			}
		}
		}catch(ExpiredJwtException e) {
			log.error(e.toString());
		}
		filterChain.doFilter(request, response);
		// �Ұ� �� ������ ���� ���� ����
		SecurityContextHolder.getContext().setAuthentication(null);
	}

}
