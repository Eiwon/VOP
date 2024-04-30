package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class regularPurchaseVO {
	private int paymentId; // 결제 내역 Id
	private int deliveryInterval; // 정기 배송 기간
}
