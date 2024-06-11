package com.web.vop.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.web.vop.util.PaymentAPIUtil;

@Configuration
public class PaymentAPIConfig {
	
	// servlet 측에서 외부 사이트와 http 통신을 하기 위한 객체 생성
	@Bean
	public RestTemplate restTemplate(){
		HttpClient httpClient = HttpClientBuilder.create()
												 .setMaxConnTotal(50)
				 								 .setMaxConnPerRoute(20).build();
		HttpComponentsClientHttpRequestFactory factory = 
				new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(10000);						 
		factory.setHttpClient(httpClient);

		RestTemplate template = new RestTemplate(factory);
		return template;
	}
	
	// 결제 결과 조회 api를 어디서든 사용할 수 있도록 bean으로 등록 
	@Bean
	public PaymentAPIUtil paymentAPIUtil() {
		return new PaymentAPIUtil();
	}
	
}
