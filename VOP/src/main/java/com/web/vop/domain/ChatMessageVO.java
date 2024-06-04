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

	private String type; // 메시지 타입
	private String senderId; // 발신자 id
	private String roomId; // 채팅이 진행되는 방 id
	private String content; // 채팅 내용
}
