package com.web.vop.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentWrapper {
	
	// ������ �ʿ��� �����͸� �ѹ��� �ޱ� ���� ��ü
	private PaymentVO paymentVO;
	private List<OrderVO> orderList;
	private CouponVO couponVO;
}
