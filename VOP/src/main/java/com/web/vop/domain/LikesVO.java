package com.web.vop.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LikesVO {
	private int likesId;
	private int productId;
	private String memberId;
	private int reviewId;
	private int likesType;
	private Date likesDateCreated;
}
