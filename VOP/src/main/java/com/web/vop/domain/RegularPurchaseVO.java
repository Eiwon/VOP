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
	private int paymentId; // ���� ���� Id
	private int deliveryInterval; // ���� ��� �Ⱓ
}
