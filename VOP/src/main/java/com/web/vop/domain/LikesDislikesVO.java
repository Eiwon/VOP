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
public class LikesDislikesVO {
	private int likesDislikesId;
	private String memberId;
	private int reviewId;
	private int LikeDislikeType;
	private Date likesDislikesDateCreated;
}
