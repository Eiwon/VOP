package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderViewDTO {
	private OrderVO orderVO;
	private int orderId;
	private String imgPath;
	private String imgChangeName;
	private String imgUrl; 
	private String formattedExpectedDeliveryDate;
}
