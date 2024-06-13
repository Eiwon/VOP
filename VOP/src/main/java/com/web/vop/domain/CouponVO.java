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
	private int couponId;
	private String couponName;
	private int discount;
	private Date dateCreated;
	private int publishing;
}
