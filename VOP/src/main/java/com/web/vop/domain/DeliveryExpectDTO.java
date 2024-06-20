package com.web.vop.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryExpectDTO {

	private String memberId;
	private int orderId;
	private int paymentId;
	int productId;
	private Date expectDeliveryDate;
	
}//end DeliveryExpiryDTO 
