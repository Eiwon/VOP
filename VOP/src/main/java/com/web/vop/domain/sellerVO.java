package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class sellerVO {
	private String memberId; // 회원 Id
	private String businessName; // 사업체 이름
	private String requestTime; // 요청 시간
	private String requestContent; // 요청 내용
}
