package com.web.vop.persistence;

public interface Constant {
	
	public static final String AUTH_ADMIN = "관리자";
	public static final String AUTH_SELLER = "판매자";
	public static final String AUTH_NORMAL = "일반";
	
	// 모두 접근 가능
	public static final String[] PERMIT_ALL = {
		"/board/main", "/member", "/basket"
	}; 
	// 로그인한 유저만 접근 가능
	public static final String[] MEMBER_ONLY = {
		"/board/mypage", "/board/orderlist", "/delivery/**"
	};
	// 판매자만 접근 가능
	public static final String[] SELLER_ONLY = {
		"/product/myProduct"	
	};
	// 관리자만 접근 가능
	public static final String[] ADMIN_ONLY = {
		"/seller/admin", "/seller/popupSellerDetails"	
	};
	// 판매자 & 관리자
	public static final String[] SELLER_OVER = {
		"/seller/sellerRequest"
	};
	
	
	public static final String STATE_APPROVAL_WAIT = "승인 대기중";
	public static final String STATE_APPROVED = "승인";
	public static final String STATE_SELL = "판매중";
	public static final String STATE_STOP = "판매 중단";
	public static final String STATE_REMOVE_WAIT = "삭제 대기중";
	
	
}
