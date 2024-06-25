package com.web.vop.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipExpiryDTO {

	private String memberId;
	private Date expiryDate;
	private String memberEmail;
	
}// end MembershipExpiryDTO()
