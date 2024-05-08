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
	private String memberId; // 회원 Id
	private int productId; // 상품 Id 
	private int productNum; // 구매 수량
}
