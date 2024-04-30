package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ImageVO {
	private int imgId;
	private int productId;
	private String imgPath;
	private String imgRealName;
	private String imgChangeName;
	private String imgExtension;
	private Date imgDateCreated;
	private int reviewId;
}
