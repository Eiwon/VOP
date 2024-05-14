package com.web.vop.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentWrapper {
	
	// 결제시 필요한 데이터를 한번에 받기 위한 객체
	private PaymentVO paymentVO;
	private List<OrderVO> orderList;
	private CouponVO couponVO;
}
