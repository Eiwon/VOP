package com.web.vop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentVO {
	private int paymentId;
	private String memberId;
	private String deliveryAddress;
	private String receiverName;
	private String receiverPhone;
	private String requirement;
	private int membershipDiscount;
	private int couponDiscount;
	private int deliveryPrice;
	private int chargePrice;
	private String chargeId;
}
