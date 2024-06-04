package com.web.vop.config;

import java.util.HashMap;

import java.util.Map;

import javax.websocket.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.web.vop.socket.AlarmHandler;
import com.web.vop.socket.ConsultHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	
	@Override 
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) { 
		registry.addHandler(alarmHandler(), "/alarm"); // 핸들러 등록
		registry.addHandler(consultHandler(), "/consult");
	}
	

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		// 메세지의 크기 제한 설정
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(8192);
		container.setMaxBinaryMessageBufferSize(8192);
		return container;
	} // end createWebSocketContainer
	
	@Bean
	public WebSocketHandler alarmHandler() {
		return new AlarmHandler();
	} // end alarmHandler
	
	@Bean
	public WebSocketHandler consultHandler() {
		return new ConsultHandler();
	} // end consultHandler
	
	@Bean
	public Map<String, WebSocketSession> alarmConnMap(){
		// alarm 웹소켓에 연결된 유저를 관리하기 위한 맵
		return new HashMap<>();
	} // end alarmConnMap
	
	// { 방에 있는 멤버들을 <key : memberId, value : Session(연결 정보)> 쌍으로 관리하는 Map }
	// 을 <key : roomId, value : 방에 속한 멤버의 연결정보 > 쌍으로 관리하는 roomList 
	@Bean
	public Map<String, Map<String, WebSocketSession>> consultRoomList(){
		return new HashMap<>();
	} // end consultRoomList
		
}
