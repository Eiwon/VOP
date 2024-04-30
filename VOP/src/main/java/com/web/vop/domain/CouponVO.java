package com.web.vop.domain;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CouponVO {
	private String memberId;
	private String couponName;
	private Date validPeriod;
	private int discount;
	private int couponNum;
}
