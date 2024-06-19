package com.web.vop.config;

public interface SecurityConfigConstants {

	public static final String AUTH_ADMIN = "������";
	public static final String AUTH_SELLER = "�Ǹ���";
	public static final String AUTH_MEMBERSHIP = "�����";
	public static final String AUTH_NORMAL = "�Ϲ�";
	
	public static final String ROLE_ADMIN = "ROLE_������";
	public static final String ROLE_SELLER = "ROLE_�Ǹ���";
	public static final String ROLE_MEMBERSHIP = "ROLE_�����";
	public static final String ROLE_NORMAL = "ROLE_�Ϲ�";

	public static final String ROLE_HIERARCHY 
		= ROLE_ADMIN + " > " + ROLE_SELLER + " > " + ROLE_NORMAL + "\n" +
		  ROLE_MEMBERSHIP + " > " + ROLE_NORMAL
		;
	
	// ��α��θ� ���� ����
	public static final String[] ANONYMOUS_ONLY = {
		"/member/register", "/member/findAccount**", "/member/findPassword**"
	};
	
	// ��� ���� ����
	public static final String[] PERMIT_ALL = {
		"/board/main", "/board/popupNotice", "/product/detail", "/product/search", 
		"/review/list", "/member/login"
	}; 
	// �α����� ������ ���� ����
	public static final String[] NORMAL_OVER = {
		"/board/mypage", "/board/orderlist", "/delivery/**", "/member/modify", "/order/**",
		"/payment/**", "/review/modify", "/review/register", "/member/logout",
		"/basket/**", "/seller/sellerRequest"
	};
	
	// ������, �Ǹ��ڰ� �ƴ� ������ ���� ����
	public static final String[] NOT_SELLER_OVER = {
			"/seller/sellerRequest"
	};
	
	// �Ǹ��ڸ� ���� ����
	public static final String[] SELLER_ONLY = {
			
	};
	// �����ڸ� ���� ����
	public static final String[] ADMIN_ONLY = {
		"/seller/admin", "/seller/popupSellerDetails"	
	};
	// �Ǹ��� & ������
	public static final String[] SELLER_OVER = {
		"/product/popup**", "/product/myProduct", "/product/register"
	};
	
	// ���� ��� url
	public static final String PERMIT_IMG_SRC = "https://vop-s3-bucket.s3.ap-northeast-2.amazonaws.com https://developers.kakao.com";
	public static final String PERMIT_SCRIPT_SRC = "'self' https://code.jquery.com/jquery-3.7.1.js https://cdn.iamport.kr";
	
}
