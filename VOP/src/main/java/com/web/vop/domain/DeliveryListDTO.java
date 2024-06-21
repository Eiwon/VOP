package com.web.vop.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryListDTO {
	
	private int paymentId;
	private String receiverName;
	private String deliveryAddress;
	private String requirement;
	private Date expectDeliveryDate;
	private String formattedExpectDeliveryDate; // ���˵� ��¥�� ������ �ʵ� �߰�
	
	 public String getFormattedExpectDeliveryDate() {
	        return formattedExpectDeliveryDate;
	    }

	    public void setFormattedExpectDeliveryDate(String formattedExpectDeliveryDate) {
	        this.formattedExpectDeliveryDate = formattedExpectDeliveryDate;
	    }
}
