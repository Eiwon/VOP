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
public class EmailAuthenticationToken {
	private String authCode;
	private Date expireTime;
}
