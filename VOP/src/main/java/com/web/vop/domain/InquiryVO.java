package com.web.vop.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class InquiryVO {
	private int inquiryId;
	private String memberId;
	private int productId;
	private String inquiryContent;
	private Date inquiryDateCreated;
	private int parentId;
}
