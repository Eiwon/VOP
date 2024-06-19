package com.web.vop.util;

public interface Constant {
	
	public static final String AUTH_ADMIN = "관리자";
	public static final String AUTH_SELLER = "판매자";
	public static final String AUTH_MEMBERSHIP = "멤버십";
	public static final String AUTH_NORMAL = "일반";
	
	
	
	
	public static final String STATE_APPROVAL_WAIT = "승인 대기중";
	public static final String STATE_APPROVED = "승인";
	public static final String STATE_SELL = "판매중";
	public static final String STATE_STOP = "판매 중단";
	public static final String STATE_REMOVE_WAIT = "삭제 대기중";
	
	public static final String DEFAULT_IMG_PATH = "nodata.jpg";
	
	public static final String ALERT_PATH = "access/alert";
	
	public static final int IS_USED = 1;
	public static final int IS_UNUSED = 0;
	
	public static final int DELIVERY_DEFAULT = 1;
	public static final int DELIVERY_NOT_DEFAULT = 0;
	
}
