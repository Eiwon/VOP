package com.web.vop.persistence;

public interface Constant {
	
	public static final String AUTH_ADMIN = "������";
	public static final String AUTH_SELLER = "�Ǹ���";
	public static final String AUTH_NORMAL = "�Ϲ�";
	
	// ��� ���� ����
	public static final String[] PERMIT_ALL = {
		"/board/main", "/member", "/basket"
	}; 
	// �α����� ������ ���� ����
	public static final String[] MEMBER_ONLY = {
		"/board/mypage", "/board/orderlist", "/delivery/**"
	};
	// �Ǹ��ڸ� ���� ����
	public static final String[] SELLER_ONLY = {
		"/product/myProduct"	
	};
	// �����ڸ� ���� ����
	public static final String[] ADMIN_ONLY = {
		"/seller/admin", "/seller/popupSellerDetails"	
	};
	// �Ǹ��� & ������
	public static final String[] SELLER_OVER = {
		"/seller/sellerRequest"
	};
	
	
	public static final String STATE_APPROVAL_WAIT = "���� �����";
	public static final String STATE_APPROVED = "����";
	public static final String STATE_SELL = "�Ǹ���";
	public static final String STATE_STOP = "�Ǹ� �ߴ�";
	public static final String STATE_REMOVE_WAIT = "���� �����";
	
	
}
