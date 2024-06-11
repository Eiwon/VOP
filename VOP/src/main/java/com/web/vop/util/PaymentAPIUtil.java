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

	// REST Template 
	
	@Autowired
	public RestTemplate restTemplate;
	
	@Autowired
	public ObjectMapper objectMapper;
	
	private String GET_TOKEN_URL = "https://api.iamport.kr/users/getToken";
	
	private String GET_PAYMENT_URL = "https://api.iamport.kr/payments/";
	
	private String CANCEL_PAYMENT_URL = "https://api.iamport.kr/payments/cancel";
	
	private String getAccessToken() {

		String accessToken = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		Map<String, String> body = new HashMap<>();
		body.put("imp_key", ApiKey.PAYMENT_API_KEY);
		body.put("imp_secret", ApiKey.PAYMENT_SECRET_KEY);
		String jsonBody = null;
		try {
			jsonBody = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
		
		String url = GET_TOKEN_URL;
		
		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
		log.info(response);
		
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
