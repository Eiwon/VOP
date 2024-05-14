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
public class OrderVO {
	private int orderId;
	private int paymentId;
	private int productId;
	private String productName;
	private int productPrice;
	private int purchaseNum;
	private Date expectDeliveryDate;
	private int imgId;
	private String memberId;
}
