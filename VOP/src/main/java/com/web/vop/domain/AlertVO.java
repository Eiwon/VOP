package com.web.vop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlertVO {
	// jsp에서 띄울 alert 정보를 담을 객체
	
	private String alertMsg;
	private String redirectUri;
}
