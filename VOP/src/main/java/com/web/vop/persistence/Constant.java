package com.web.vop.persistence;

import org.springframework.http.HttpMethod;

public interface Constant {
	
	public static final String AUTH_ADMIN = "������";
	public static final String AUTH_SELLER = "�Ǹ���";
	public static final String AUTH_NORMAL = "�Ϲ�";
	
	// ��α��θ� ���� ����
	public static final String[] ANONYMOUS_ONLY = {
		"/member/login", "/member/register", "/member/findAccount**", "/member/findPassword**"
	};
	
	// ��� ���� ����
	public static final String[] PERMIT_ALL = {
		"/board/main", "/board/popupNotice", "/product/detail", "/product/search", 
		"/review/list"
	}; 
	// �α����� ������ ���� ����
	public static final String[] MEMBER_ONLY = {
		"/board/mypage", "/board/orderlist", "/delivery/**", "/member/modify", "/order/**",
		"/payment/**", "/review/modify", "/review/register", "/seller/sellerRequest", "/member/logout",
		"/basket/**"
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
	
	
	public static final String STATE_APPROVAL_WAIT = "���� �����";
	public static final String STATE_APPROVED = "����";
	public static final String STATE_SELL = "�Ǹ���";
	public static final String STATE_STOP = "�Ǹ� �ߴ�";
	public static final String STATE_REMOVE_WAIT = "���� �����";
	
	public static final String DEFAULT_IMG_PATH = "nodata.jpg";
	
	public static final String ALERT_PATH = "access/alert";
	
	public static final int IS_USED = 1;
	public static final int IS_UNUSED = 0;
}
