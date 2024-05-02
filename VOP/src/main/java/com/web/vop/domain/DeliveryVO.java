package com.web.vop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryVO {
	private int deliveryId; // 배송지 Id
	private String memberId; // 회원 Id
	private String receiverName; // 수령인 이름
	private String receiverAddress; // 배송지
	private String receiverPhone; // 수령인 전화번호 
	private String requirement; //  요청사항
	
}
