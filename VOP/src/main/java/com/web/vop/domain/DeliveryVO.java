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
	private int deliveryId; // ����� Id
	private String memberId; // ȸ�� Id
	private String receiverName; // ������ �̸�
	private String receiverAddress; // �����
	private String receiverPhone; // ������ ��ȭ��ȣ 
	private String requirement; //  ��û����
	
}
