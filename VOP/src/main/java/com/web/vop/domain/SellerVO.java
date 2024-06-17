package com.web.vop.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SellerVO{
	private String memberId; // 회원 Id
	private String businessName; // 사업체 이름
	private Date requestTime; // 요청 시간
	private String requestContent; // 요청 내용
	private String requestState; // 요청 승인 대기중, 거절 여부
	private String refuseMsg; // 요청 거부 사유
}