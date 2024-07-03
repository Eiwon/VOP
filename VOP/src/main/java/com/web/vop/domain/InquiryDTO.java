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
public class InquiryDTO {
	private int inquiryId;
	private String inquiryMemberId;
	private int productId;
	private String inquiryContent;
	private Date inquiryDateCreated;
	private int answerId;
	private String answerMemberId;
	private String answerContent;
	private Date answerDateCreated;
}
