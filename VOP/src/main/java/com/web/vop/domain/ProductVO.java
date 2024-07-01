package com.web.vop.domain;

import java.util.Date;
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
	private int reviewNum;
	private int productRemains;
	private String productPlace;
	private String productState	;
	private String category;
	private int imgId;
	private Date productDateCreated;
	private float reviewAvg;
}
