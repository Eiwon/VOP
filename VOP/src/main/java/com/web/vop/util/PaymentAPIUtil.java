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

	/* api ���� Ȯ�� �ּ� : https://developers.portone.io/api/rest-v1/auth
	* � url�� �޼ҵ�� � �����͸� ����ϴ��� Ȯ�� ����
	* Ŭ���̾�Ʈ ������ ajax�� ������ ����������, ���� ���� java�� �ٸ� ����� ����մϴ� 
	*/
	
	// REST Template (�ڹٿ��� REST ��û�� ������ ���� Ŭ����)
	@Autowired
	public RestTemplate restTemplate;
	
	// java ��ü <-> json ��ȯ ����� �����ϴ� ��ü
	@Autowired
	public ObjectMapper objectMapper;
	
	// ���� ��ū �߱� URL
	private String GET_TOKEN_URL = "https://api.iamport.kr/users/getToken";
	
	// ���� ���� ��ȸ URL
	private String GET_PAYMENT_URL = "https://api.iamport.kr/payments/";
	
	// ���� ��� URL
	private String CANCEL_PAYMENT_URL = "https://api.iamport.kr/payments/cancel";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String getAccessToken() {
		log.info("��ū �߱�");
		String accessToken = null;
		
		// api ������ ���� ��� headers, body �� �Է�
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		Map<String, String> body = new HashMap<>();
		body.put("imp_key", ApiKey.PAYMENT_API_KEY);
		body.put("imp_secret", ApiKey.PAYMENT_SECRET_KEY);
		
		// API���� JSON�� �䱸�ϴ� JSON���� ��ȯ
		String jsonBody = null;
		try {
			jsonBody = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
		
		// ���� �� ������ json -> map ��ü�� ��ȯ
		ResponseEntity<Map> response = restTemplate.exchange(GET_TOKEN_URL, HttpMethod.POST, entity, Map.class);
		log.info(response);
		
		// access token ����
		accessToken = ((Map<String, Object>)response.getBody().get("response")).get("access_token").toString();
		log.info(accessToken);
		
		return accessToken;
	} // end getAccessToken;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getPaymentAmount(String impUid) {
		log.info("���� �ݾ� ��ȸ GET ");
		String accessToken = getAccessToken();
		String url = GET_PAYMENT_URL + impUid;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", accessToken);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
		log.info("�ǰ��� ��� : " + response);
		int chargePrice = Integer.parseInt(((Map<String, Object>)response.getBody().get("response")).get("amount").toString());
		
		return chargePrice;
	} // end getPaymentAmount
	
	@SuppressWarnings("rawtypes")
	public void cancelPayment(String impUid) {
		log.info("���� ��� POST");
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
		log.info("���� ��� :" + response);
		
	} // end cancelPayment
	
}
