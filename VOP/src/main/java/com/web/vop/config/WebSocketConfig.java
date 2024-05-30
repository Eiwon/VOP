package com.web.vop.config;

import java.util.HashMap;

import java.util.Map;

//import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.web.vop.socket.AlarmHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	
	@Override 
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) { 
		registry.addHandler(alarmHandler(), "/alarm"); // �ڵ鷯 ���
	}
	

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		// �޼����� ũ�� ���� ����
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//		container.setMaxTextMessageBufferSize(8192);
//		container.setMaxBinaryMessageBufferSize(8192);
		return container;
	} // end createWebSocketContainer

	
	@Bean
	public WebSocketHandler alarmHandler() {
		return new AlarmHandler();
	} // end alarmHandler
	
	@Bean
	public Map<String, WebSocketSession> alarmConnMap(){
		// alarm �����Ͽ� ����� ������ �����ϱ� ���� ��
		return new HashMap<String, WebSocketSession>();
	} // end alarmConnMap
}
