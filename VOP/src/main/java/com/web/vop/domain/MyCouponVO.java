package com.web.vop.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MyCouponVO {
	// CouponPocket ���̺�� Coupon ���̺��� ������ ������
	// (�� ���� �� ����)�� ��� ���� VO
	
	private int couponId;
	private String couponName;
	private int discount;
	private Date expirationDate;
	private int isUsed;
	
}
