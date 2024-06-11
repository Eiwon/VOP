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
	
	// servlet ������ �ܺ� ����Ʈ�� http ����� �ϱ� ���� ��ü ����
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
	
	// ���� ��� ��ȸ api�� ��𼭵� ����� �� �ֵ��� bean���� ��� 
	@Bean
	public PaymentAPIUtil paymentAPIUtil() {
		return new PaymentAPIUtil();
	}
	
}
