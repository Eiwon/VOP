package com.web.vop.config;

import java.util.HashMap;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.web.vop.domain.ChatRoom;
import com.web.vop.socket.AlarmHandler;
import com.web.vop.socket.ConsultHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	
	@Override 
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) { 
		registry.addHandler(alarmHandler(), "/alarm"); // �ڵ鷯 ���
		registry.addHandler(consultHandler(), "/consult");
	}
	
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		// �޼����� ũ�� ���� ����
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
		// alarm �����Ͽ� ����� ������ �����ϱ� ���� ��
		return new HashMap<>();
	} // end alarmConnMap
	
	@Bean
	public Map<String, ChatRoom> consultRoomList(){
		return new HashMap<>();
	} // end consultRoomList
		
}
