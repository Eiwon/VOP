package com.web.vop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductVO {
	private int productId;
	private String memberId;
	private String productName;
	private int productPrice;
	private int review_num;
	private int product_remains;
	private String product_place;
	private String product_state;
}
