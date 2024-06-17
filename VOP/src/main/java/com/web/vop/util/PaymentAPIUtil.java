package com.web.vop.util;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.config.ApiKey;

import lombok.extern.log4j.Log4j;

@Log4j
public class PaymentAPIUtil {

	// api 정보 확인 주소 : https://developers.portone.io/api/rest-v1/auth
	
	// REST Template 
	@Autowired
	public RestTemplate restTemplate;
	
	// java 객체 <-> json 변환 기능을 제공하는 객체
	@Autowired
	public ObjectMapper objectMapper;
	
	// 인증 토큰 발급 URL
	private String GET_TOKEN_URL = "https://api.iamport.kr/users/getToken";
	
	// 결제 정보 조회 URL
	private String GET_PAYMENT_URL = "https://api.iamport.kr/payments/";
	
	// 결제 취소 URL
	private String CANCEL_PAYMENT_URL = "https://api.iamport.kr/payments/cancel";
	
	private String getAccessToken() {

		String accessToken = null;
		
		// api 설명서에 나온 대로 headers, body 값 입력
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		Map<String, String> body = new HashMap<>();
		body.put("imp_key", ApiKey.PAYMENT_API_KEY);
		body.put("imp_secret", ApiKey.PAYMENT_SECRET_KEY);
		
		// 맵 객체를 json 타입으로 변환
		String jsonBody = null;
		try {
			jsonBody = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
		
		// 전송 후 응답을 json -> map 객체로 변환
		ResponseEntity<Map> response = restTemplate.exchange(GET_TOKEN_URL, HttpMethod.POST, entity, Map.class);
		log.info(response);
		
		// access token 추출
		accessToken = ((Map<String, Object>)response.getBody().get("response")).get("access_token").toString();
		log.info(accessToken);
		
		return accessToken;
	} // end getAccessToken;
	
	public int getPaymentAmount(String impUid) {
		log.info("결제 금액 조회 GET ");
		String accessToken = getAccessToken();
		String url = GET_PAYMENT_URL + impUid;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", accessToken);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
		log.info(response);
		int chargePrice = Integer.parseInt(((Map<String, Object>)response.getBody().get("response")).get("amount").toString());
		
		return chargePrice;
	} // end getPaymentAmount
	
	public void cancelPayment(String impUid) {
		log.info("결제 취소 POST");
		String accessToken = getAccessToken();
		String url = CANCEL_PAYMENT_URL;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", accessToken);

		Map<String, String> body = new HashMap<>();
		body.put("imp_uid", impUid);
		String jsonBody = null;
		try {
			jsonBody = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
		
		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
		log.info("결제 취소 :" + response);
		
	} // end cancelPayment
	
}
