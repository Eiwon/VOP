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
	// CouponPocket 테이블과 Coupon 테이블을 조인한 데이터
	// (내 쿠폰 상세 정보)를 담기 위한 VO
	
	private int couponId;
	private String couponName;
	private int discount;
	private Date expirationDate;
	private int isUsed;
	
}
