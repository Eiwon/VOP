package com.web.vop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegularPurchaseVO {
	private int paymentId; // 결제 내역 Id
	private int deliveryInterval; // 정기 배송 기간
}
