package com.web.vop.domain;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AnswerVO {
	private int answerId;
	private String memberId;
	private int productId;
	private String answerContent;
	private Date answerDateCreated;
	private int inquiryId;
}
