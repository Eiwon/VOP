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
<<<<<<< HEAD
	
=======
>>>>>>> ef0c7002d5dac7e68e9f6991cfb1945946ccedbb
}
