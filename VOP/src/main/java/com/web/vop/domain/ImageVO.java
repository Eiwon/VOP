package com.web.vop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
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
}