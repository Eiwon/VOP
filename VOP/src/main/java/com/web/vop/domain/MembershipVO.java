package com.web.vop.domain;

import java.sql.Date;

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
public class MembershipVO {

	private int membershipId;
	private String memberId;
	private Date joinDate;
	private Date expiryDate;
	private Date paymentDate;
	
}
