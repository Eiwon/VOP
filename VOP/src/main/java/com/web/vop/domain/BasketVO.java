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
@AllArgsConstructor
public class BasketVO {
	private String memberId; // ȸ�� Id
	private int productId; // ��ǰ Id 
	private int productNum; // ���� ����
}
