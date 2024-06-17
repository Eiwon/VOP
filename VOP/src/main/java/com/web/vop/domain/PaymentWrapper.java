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
	private MemberVO memberVO;
	private DeliveryVO deliveryVO; 
	private List<OrderViewDTO> orderList;
	private MyCouponVO myCouponVO;
	private MembershipVO membershipVO;

}
