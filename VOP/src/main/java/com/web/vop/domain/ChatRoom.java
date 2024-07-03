package com.web.vop.domain;

import lombok.Setter;
import lombok.ToString;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatRoom {
	private String roomId;
	private int state;
	private Map<String, WebSocketSession> memberList; // key : memberId, value : 연결정보
}
