package com.web.vop.persistence;

import org.springframework.http.HttpMethod;

public interface Constant {
	
	public static final String AUTH_ADMIN = "관리자";
	public static final String AUTH_SELLER = "판매자";
	public static final String AUTH_NORMAL = "일반";
	
	// 비로그인만 접근 가능
	public static final String[] ANONYMOUS_ONLY = {
		"/member/login", "/member/register", "/member/findAccount**", "/member/findPassword**"
	};
	
	// 모두 접근 가능
	public static final String[] PERMIT_ALL = {
		"/board/main", "/board/popupNotice", "/product/detail", "/product/search", 
		"/review/list"
	}; 
	// 로그인한 유저만 접근 가능
	public static final String[] MEMBER_ONLY = {
		"/board/mypage", "/board/orderlist", "/delivery/**", "/member/modify", "/order/**",
		"/payment/**", "/review/modify", "/review/register", "/seller/sellerRequest", "/member/logout",
		"/basket/**"
	};
	// 판매자만 접근 가능
	public static final String[] SELLER_ONLY = {
			
	};
	// 관리자만 접근 가능
	public static final String[] ADMIN_ONLY = {
		"/seller/admin", "/seller/popupSellerDetails"	
	};
	// 판매자 & 관리자
	public static final String[] SELLER_OVER = {
		"/product/popup**", "/product/myProduct", "/product/register"
	};
	
	
	public static final String STATE_APPROVAL_WAIT = "승인 대기중";
	public static final String STATE_APPROVED = "승인";
	public static final String STATE_SELL = "판매중";
	public static final String STATE_STOP = "판매 중단";
	public static final String STATE_REMOVE_WAIT = "삭제 대기중";
	
	public static final String DEFAULT_IMG_PATH = "nodata.jpg";
	
	public static final String ALERT_PATH = "access/alert";
	
	public static final int IS_USED = 1;
	public static final int IS_UNUSED = 0;
}
