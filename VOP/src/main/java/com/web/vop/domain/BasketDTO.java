package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BasketDTO {
	private ProductPreviewDTO productPreviewDTO;
	private int productId;
	private int productNum;
}

// basket ���̺�� product ���̺��� join�� ������ ���� �뵵
