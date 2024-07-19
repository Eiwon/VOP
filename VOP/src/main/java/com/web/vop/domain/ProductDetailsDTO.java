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
	// ��ǰ �� ���� ���ۿ�
	private ProductVO productVO;
	private MemberVO memberVO;
	private String businessName;
	private ImageVO thumbnail;
	private String thumbnailUrl;
	private List<ImageVO> details;
	private List<String> detailsUrl;
	
}
