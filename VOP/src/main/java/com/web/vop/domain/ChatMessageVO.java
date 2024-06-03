package com.web.vop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatMessageVO {

	private String type; // �޽��� Ÿ��
	private String senderId; // �߽��� id
	private String roomId; // ä���� ����Ǵ� �� id
	private String content; // ä�� ����
}
