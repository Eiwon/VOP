package com.web.vop.domain;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DeliveryExpectDTO {

	private String memberId;
	private int orderId;
	private int paymentId;
	int productId;
	private Date expectDeliveryDate;
	private String memberEmail;
	
}//end DeliveryExpiryDTO 
