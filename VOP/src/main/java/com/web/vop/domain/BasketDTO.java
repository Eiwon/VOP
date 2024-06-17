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

// basket 테이블과 product 테이블을 join한 데이터 전달 용도
