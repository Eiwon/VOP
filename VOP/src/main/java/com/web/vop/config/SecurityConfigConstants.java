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
			"/member/register", "/member/login", "/member/loginFail", "/member/findAccount**",
			"/member/findPassword**", "/member/idDupChk", "/member/phoneDupChk", "/member/findByPhone"
			};

	// 모두 접근 가능
	public static final String[] PERMIT_ALL = { 
			"/member/login", "/member/mailAuthentication", "/member/changePw",
			"/board/main", "/board/popupNotice", 
			"/popupAds/popupAds", "/popupAds/blockPopup", "/popupAds/myPopupAds",
			"/product/detail", "/product/search", "/product/bestReview", "/product/recent", 
			"/review/list" };
	// 로그인한 유저만 접근 가능
	public static final String[] NORMAL_OVER = { 
			"/member/check", "/member/modify", "/member/logout",
			"/board/mypage", "/board/orderlist", "/board/inquiry", "/board/myInfo", "/board/basket", 
			"/board/delivery", "/board/consult",
			"/coupon/**",
			"/delivery/**",
			"/order/**", 
			"/payment/**", 
			"/review/modify", "/review/register", 
			"/basket/**", 
			"/seller/sellerRequest", 
			"/membership/**" };

	// 판매자만 접근 가능
	public static final String[] SELLER_ONLY = {
	};
	// 관리자만 접근 가능
	public static final String[] ADMIN_ONLY = { 
			"/member/auth", 
			"/board/admin", 
			"/coupon/list", "/coupon/register", "/coupon/delete", "/coupon/publish**",
			"/popupAds/main", "/popupAds/register", "/popupAds/update", "/popupAds/delete", "/popupAds/list",
			"/product/popupDetails", "/product/delete", "/product/registerRequest", "/product/deleteRequest",
			"/seller/admin", "/seller/approval", "/seller/delete", "/seller/wait", "/seller/approved", 
			"/seller/popupSellerDetails", "/seller/popupRegisterNotice" };
	
	// 판매자 이상만 접근 가능
	public static final String[] SELLER_OVER = { 
			"/product/popupUpdate", "/product/myProduct", "/product/register", "/product/myList", "/product/update",
			"/product/changeState", "/product/request",
			"/seller/main", "/seller/registerProduct", "/seller/myProduct"  };

	// 참조 허용 url
	public static final String PERMIT_IMG_SRC = "https://vop-s3-bucket.s3.ap-northeast-2.amazonaws.com https://developers.kakao.com";
	public static final String PERMIT_SCRIPT_SRC = "'self' https://code.jquery.com/jquery-3.7.1.js https://cdn.iamport.kr";
	
}
