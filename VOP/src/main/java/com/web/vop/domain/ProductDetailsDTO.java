package com.web.vop.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDetailsDTO {
	// 상품 상세 정보 전송용
	private int productId;
	private String memberId;
	private String productName;
	private int productPrice;
	private int productRemains;
	private String productPlace;
	private String productState;
	private String category;
	private int imgId;
	private Date productDateCreated;
	private String memberName;
	private String memberEmail;
	private String memberPhone;
	private String businessName;
	private List<Integer> imgIdDetails;
}
