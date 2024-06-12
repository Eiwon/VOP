package com.web.vop.controller;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.services.cognitoidp.model.HttpHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.web.vop.config.ApiKey;
import com.web.vop.domain.MessageVO;
import com.web.vop.service.MessageService;
import com.web.vop.socket.AlarmHandler;
import com.web.vop.util.PaymentAPIUtil;

import lombok.extern.log4j.Log4j;

@RequestMapping("/board")
@Controller
@Log4j
public class BoardController {// 메인 페이지 구현 컨트롤러

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private PaymentAPIUtil paymentAPIUtil;
	
	@GetMapping("/main") 
	public void mainGET() {
		System.out.println("main.jsp 이동");
		log.info("mainGET()");
	}//end mainGET()
	
	
	@PostMapping("/main")
	public String mainPOST() {
		log.info("mainPOST()");
		
		return "redirect:/board/main";
	}
	
	// 마이페이지 호출하기 -> 세션이 있을 경우 이동, 없으면 로그인 페이지로 이동
	@GetMapping("/mypage") 
	public void mypageGET() {
		System.out.println("mypage.jsp 이동");
		log.info("mypageGET()");
	}//end mypageGET()
	
	
	// 고객센터 호출하기
	@GetMapping("inquiry") 
	public void inquiryGET() {
		System.out.println("inquiry.jsp 이동");
		log.info("inquiryGET()");
	}//end inquiryGET()
	
	
	@PostMapping("/inquiry")
	public String inquiryPOST() {
		log.info("inquiryPOST()");
		
		return "redirect:/board/inquiry";
	}
	
	@GetMapping("/myInfo")
	public String myInfoGET() {
		log.info("member/modify로 redirect");
		return "redirect:../member/modify";
	} // end myInfoGet
	
	@GetMapping("/seller")
	public String sellerGET() {
		log.info("seller/sellerRequest로 redirect");
		return "redirect:../seller/sellerRequest";
	} // end myInfoGet
	
	@GetMapping("/admin")
	public String adminGET() {
		log.info("관리자 페이지로 이동");
		return "redirect:../seller/admin";
	} // end myInfoGet
	
	@GetMapping("/basket")
	public String basketGET() {
		log.info("장바구니 페이지 이동");
		return "redirect:../basket/main";
	} // end basketGET
	
	@GetMapping("/delivery")
	public String deliveryGET() {
		log.info("delivery controller로 redirection");
		return "redirect:../Delivery/deliveryAddressList";
	} // end deliveryGET
	
	@GetMapping("/notice")
	@ResponseBody
	public ResponseEntity<List<MessageVO>> noticeGET() {
		log.info("모든 공지사항 요청");
		List<MessageVO> result = messageService.getNotice();
		return new ResponseEntity<>(result, HttpStatus.OK);
	} // end noticeGET
	
	@GetMapping("/popupNotice")
	public void popupNoticeGET(Model model, int messageId) {
		log.info("공지사항 정보 요청 : " + messageId);
		MessageVO message = messageService.getById(messageId);
		log.info(message);
		model.addAttribute("messageVO", message);
	} // end popupNoticeGET
	
	
	@GetMapping("/consult")
	public String consultGET(Model model, String roomId) {
		log.info("consultGET");
		log.info("request room Id : " + roomId);
		
		model.addAttribute("roomId", roomId);
		
		return "chat/consult";
	} // end consultGET
	
}//end MainController
