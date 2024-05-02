package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SellerVO{
	private String memberId; // ȸ�� Id
	private String businessName; // ���ü �̸�
	private String requestTime; // ��û �ð�
	private String requestContent; // ��û ����
	private String requestState; // ��û ���� �����, ���� ����
	private String refuseMsg; // ��û �ź� ����
}
