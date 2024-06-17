package com.web.vop.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SellerVO{
	private String memberId; // ȸ�� Id
	private String businessName; // ���ü �̸�
	private Date requestTime; // ��û �ð�
	private String requestContent; // ��û ����
	private String requestState; // ��û ���� �����, ���� ����
	private String refuseMsg; // ��û �ź� ����
}