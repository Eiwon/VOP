package com.web.vop.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewVO {
	private int reviewId;
	private String memberId;
	private int productId;
	private String reviewContent;
	private Date reviewDateCreated;
	private float reviewStar;
	private int reviewLike;
	private int imgId;
}
