package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductPreviewDTO {

	private ProductVO productVO;
	private String imgPath;
	private String imgChangeName;
	private String imgUrl;
}
