package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BasketDTO extends ProductVO{
	private int productNum;
}

// basket ���̺�� product ���̺��� join�� ������ ���� �뵵
