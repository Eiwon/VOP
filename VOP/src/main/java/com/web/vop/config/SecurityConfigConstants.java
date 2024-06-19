package com.web.vop.config;

public interface SecurityConfigConstants {

	public static final String AUTH_ADMIN = "관리자";
	public static final String AUTH_SELLER = "판매자";
	public static final String AUTH_MEMBERSHIP = "멤버십";
	public static final String AUTH_NORMAL = "일반";
	
	public static final String ROLE_ADMIN = "ROLE_관리자";
	public static final String ROLE_SELLER = "ROLE_판매자";
	public static final String ROLE_MEMBERSHIP = "ROLE_멤버십";
	public static final String ROLE_NORMAL = "ROLE_일반";

	public static final String ROLE_HIERARCHY 
		= ROLE_ADMIN + " > " + ROLE_SELLER + " > " + ROLE_NORMAL + "\n" +
		  ROLE_MEMBERSHIP + " > " + ROLE_NORMAL
		;
	
	// 비로그인만 접근 가능
	public static final String[] ANONYMOUS_ONLY = {
		"/member/register", "/member/findAccount**", "/member/findPassword**"
	};
	
	// 모두 접근 가능
	public static final String[] PERMIT_ALL = {
		"/board/main", "/board/popupNotice", "/product/detail", "/product/search", 
		"/review/list", "/member/login"
	}; 
	// 로그인한 유저만 접근 가능
	public static final String[] NORMAL_OVER = {
		"/board/mypage", "/board/orderlist", "/delivery/**", "/member/modify", "/order/**",
		"/payment/**", "/review/modify", "/review/register", "/member/logout",
		"/basket/**", "/seller/sellerRequest"
	};
	
	// 관리자, 판매자가 아닌 유저만 접근 가능
	public static final String[] NOT_SELLER_OVER = {
			"/seller/sellerRequest"
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
	
	// 참조 허용 url
	public static final String PERMIT_IMG_SRC = "https://vop-s3-bucket.s3.ap-northeast-2.amazonaws.com https://developers.kakao.com";
	public static final String PERMIT_SCRIPT_SRC = "'self' https://code.jquery.com/jquery-3.7.1.js https://cdn.iamport.kr";
	
}
