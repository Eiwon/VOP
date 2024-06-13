package com.web.vop.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.AlertVO;
import com.web.vop.domain.MessageVO;
import com.web.vop.domain.PagingListDTO;
import com.web.vop.service.MessageService;
import com.web.vop.util.Constant;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/popupAds")
@Log4j
public class PopupAdsController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@GetMapping("/main")
	public void popupAdsMainGET() {
		log.info("팝업광고 관리 페이지 이동");
	} // end popupAdsMainGET
	
	@GetMapping("/register")
	public void popupAdsRegisterGET() {
		log.info("팝업광고 등록 페이지 이동");
	} // end popupAdsRegisterGET
	
	@PostMapping("/register")
	public String popupAdsRegisterPOST(Model model, MessageVO messageVO) {
		log.info("팝업광고 등록 : " + messageVO);
		AlertVO alertVO = new AlertVO();
		
		int res = messageService.registerMessage(messageVO);
		
		if(res == 1) {
			alertVO.setAlertMsg("등록 성공");			
		}else {
			alertVO.setAlertMsg("등록 실패");
		}
		
		alertVO.setRedirectUri("close");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end popupAdsRegisterPOST
	
	@GetMapping("/update")
	public String popupAdsUpdateGET(Model model) {
		log.info("팝업광고 수정 페이지 이동");
		return "popupAds/register";
	} // end popupAdsUpdateGET
	
	@PostMapping("/update")
	public String popupAdsUpdatePOST(Model model, MessageVO messageVO) {
		log.info("팝업광고 수정");
		AlertVO alertVO = new AlertVO();
		
		
		alertVO.setAlertMsg(null);
		alertVO.setRedirectUri("close");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end popupAdsUpdatePOST
	
	@DeleteMapping("/delete")
	public ResponseEntity<Integer> popupAdsDelete(@RequestBody List<Integer> selectedList){
		log.info("팝업광고 삭제");
		int res = 0;
		
		return new ResponseEntity<Integer>(res, HttpStatus.OK);
	} // end popupAdsDelete
	
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<PagingListDTO<MessageVO>> getAllList(Pagination pagination){
		PagingListDTO<MessageVO> pagingListDTO = new PagingListDTO<>();
		PageMaker pageMaker = new PageMaker();
		pageMaker.setPagination(pagination);
		
		pagingListDTO.setList(messageService.getAllPopup(pageMaker));
		pageMaker.update();
		pagingListDTO.setPageMaker(pageMaker);
		
		return new ResponseEntity<PagingListDTO<MessageVO>>(pagingListDTO, HttpStatus.OK);
	} // end getAllList
	
	@GetMapping("/popupAds")
	public void showPopupAds(Model model, int messageId) {
		log.info("팝업 광고 페이지 이동");
		MessageVO messageVO = messageService.getById(messageId);
		model.addAttribute("messageVO", messageVO);
	} // end showPopupAds
	
	
	@GetMapping("/blockPopup")
	@ResponseBody
	public ResponseEntity<Integer> blockPopup(int messageId, HttpServletResponse response, @CookieValue(name = "blockPopup", required = false) String cookie) {
		log.info("팝업 차단 쿠키 생성");
		Cookie newCookie = new Cookie("blockPopup", null);
		List<Integer> blockList = null;
		
		if(cookie == null) { // blockPopup 쿠키가 없으면 생성
			blockList = new ArrayList<>();
		}else { // 쿠키가 있으면 차단 목록 불러옴
			try { 
				blockList = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), ArrayList.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		blockList.add(messageId);
		String encoded = null;
		try {
			// 세션에는 String만 저장 가능하기 때문에 차단 목록을 JSON으로 변환
			// 쿠키에는 , 를 저장할 수 없어서 URL 규칙으로 인코딩
			encoded = URLEncoder.encode(objectMapper.writeValueAsString(blockList), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		newCookie.setValue((encoded == null) ? cookie : encoded);  
		
		LocalDateTime current = new LocalDateTime();
		int remainSecondOfDay = 86400 - current.getHourOfDay() *3600 - current.getMinuteOfHour() *60 - current.getSecondOfMinute(); 
		newCookie.setMaxAge(remainSecondOfDay);
		log.info("쿠키 만료 남은 시간 : " + remainSecondOfDay + "초");
		response.addCookie(newCookie);
		
		return new ResponseEntity<Integer>(1, HttpStatus.OK);
	} // end blockPopup
	
	@GetMapping("/myPopupAds")
	@ResponseBody
	public ResponseEntity<List<Integer>> getPopupAds(@CookieValue(name = "blockPopup", required = false) String cookie) {
		log.info("팝업 광고 id 호출 요청");
		
		List<Integer> blockList = null;
		
		if(cookie != null) { // 쿠키가 있으면 차단 목록 불러옴
			try { 
				blockList = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), ArrayList.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 모든 팝업광고 id 불러오기
		List<Integer> popupAdsList = messageService.getAllPopupId();
		log.info(popupAdsList);
		
		if(blockList == null) {
			return new ResponseEntity<List<Integer>>(popupAdsList, HttpStatus.OK);
		}
		
		// 불러온 팝업광고에서 차단목록 제거
		
		// popupAdsList : 12 7 6 5 4 2
		// blockList : 11 9 7 5 3 1
		// 작으면 다음 blockList와 비교, 같으면 다음 blockList + popupAdsList와 비교, 크면 result에 추가 + 다음 popupAdsList와 비교
		// blockList가 더이상 없으면 남은 popupAdsList 전부 result 추가 후 종료, popupAdsList가 더이상 없으면 종료
		
		// 내림차순 정렬
		blockList.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		
		Iterator<Integer> blockIter = blockList.iterator();
		List<Integer> resultList = new ArrayList<>();
		int blockId = 0, compare = 0;
		for(int i = 0; i < popupAdsList.size();) {
			if(blockIter.hasNext()) { // 확인할 block id가 있다면 꺼내서 비교
				blockId = blockIter.next();
				compare = popupAdsList.get(i) - blockId;
				if(compare > 0) { // 가장 큰 block id보다 크기 때문에 blockList에 존재하지 않는 값 == 차단되지 않은 id
					resultList.add(popupAdsList.get(i++));
				}else if(compare == 0) { // 차단된 id 발견시 해당 id는 result에 추가하지 않고 다음 popupAds 확인 
					i++;
				} // 가장 큰 popupAds id보다 큰 block id는 스킵
			}else { // 모든 block id를 다 확인했다면, 남은 popupAds를 전부 result에 추가
				while(i < popupAdsList.size()) {
					resultList.add(popupAdsList.get(i++));
				}
				break;
			}
		}
	
		return new ResponseEntity<List<Integer>>(resultList, HttpStatus.OK);
	} // end blockPopup
	
	
}
