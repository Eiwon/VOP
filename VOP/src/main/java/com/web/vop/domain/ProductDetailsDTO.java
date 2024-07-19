package com.web.vop.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDetailsDTO {
	// 상품 상세 정보 전송용
	private ProductVO productVO;
	private MemberVO memberVO;
	private String businessName;
	private ImageVO thumbnail;
	private String thumbnailUrl;
	private List<ImageVO> details;
	private List<String> detailsUrl;
	
}
