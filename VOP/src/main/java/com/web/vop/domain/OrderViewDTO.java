package com.web.vop.domain;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderViewDTO {
	private OrderVO orderVO;
	private String imgPath;
	private String imgChangeName;
	private String imgUrl; 
}
