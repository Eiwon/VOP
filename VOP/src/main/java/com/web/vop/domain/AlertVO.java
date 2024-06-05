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
	// jsp���� ��� alert ������ ���� ��ü
	
	private String alertMsg;
	private String redirectUri;
}
