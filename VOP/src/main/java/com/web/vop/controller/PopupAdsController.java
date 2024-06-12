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
		log.info("�˾����� ���� ������ �̵�");
	} // end popupAdsMainGET
	
	@GetMapping("/register")
	public void popupAdsRegisterGET() {
		log.info("�˾����� ��� ������ �̵�");
	} // end popupAdsRegisterGET
	
	@PostMapping("/register")
	public String popupAdsRegisterPOST(Model model, MessageVO messageVO) {
		log.info("�˾����� ��� : " + messageVO);
		AlertVO alertVO = new AlertVO();
		
		int res = messageService.registerMessage(messageVO);
		
		if(res == 1) {
			alertVO.setAlertMsg("��� ����");			
		}else {
			alertVO.setAlertMsg("��� ����");
		}
		
		alertVO.setRedirectUri("close");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end popupAdsRegisterPOST
	
	@GetMapping("/update")
	public String popupAdsUpdateGET(Model model) {
		log.info("�˾����� ���� ������ �̵�");
		return "popupAds/register";
	} // end popupAdsUpdateGET
	
	@PostMapping("/update")
	public String popupAdsUpdatePOST(Model model, MessageVO messageVO) {
		log.info("�˾����� ����");
		AlertVO alertVO = new AlertVO();
		
		
		alertVO.setAlertMsg(null);
		alertVO.setRedirectUri("close");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end popupAdsUpdatePOST
	
	@DeleteMapping("/delete")
	public ResponseEntity<Integer> popupAdsDelete(@RequestBody List<Integer> selectedList){
		log.info("�˾����� ����");
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
		log.info("�˾� ���� ������ �̵�");
		MessageVO messageVO = messageService.getById(messageId);
		model.addAttribute("messageVO", messageVO);
	} // end showPopupAds
	
	
	@GetMapping("/blockPopup")
	@ResponseBody
	public ResponseEntity<Integer> blockPopup(int messageId, HttpServletResponse response, @CookieValue(name = "blockPopup", required = false) String cookie) {
		log.info("�˾� ���� ��Ű ����");
		Cookie newCookie = new Cookie("blockPopup", null);
		List<Integer> blockList = null;
		
		if(cookie == null) { // blockPopup ��Ű�� ������ ����
			blockList = new ArrayList<>();
		}else { // ��Ű�� ������ ���� ��� �ҷ���
			try { 
				blockList = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), ArrayList.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		blockList.add(messageId);
		String encoded = null;
		try {
			// ���ǿ��� String�� ���� �����ϱ� ������ ���� ����� JSON���� ��ȯ
			// ��Ű���� , �� ������ �� ��� URL ��Ģ���� ���ڵ�
			encoded = URLEncoder.encode(objectMapper.writeValueAsString(blockList), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		newCookie.setValue((encoded == null) ? cookie : encoded);  
		
		LocalDateTime current = new LocalDateTime();
		int remainSecondOfDay = 86400 - current.getHourOfDay() *3600 - current.getMinuteOfHour() *60 - current.getSecondOfMinute(); 
		newCookie.setMaxAge(remainSecondOfDay);
		log.info("��Ű ���� ���� �ð� : " + remainSecondOfDay + "��");
		response.addCookie(newCookie);
		
		return new ResponseEntity<Integer>(1, HttpStatus.OK);
	} // end blockPopup
	
	@GetMapping("/myPopupAds")
	@ResponseBody
	public ResponseEntity<List<Integer>> getPopupAds(@CookieValue(name = "blockPopup", required = false) String cookie) {
		log.info("�˾� ���� id ȣ�� ��û");
		
		List<Integer> blockList = null;
		
		if(cookie != null) { // ��Ű�� ������ ���� ��� �ҷ���
			try { 
				blockList = objectMapper.readValue(URLDecoder.decode(cookie, "UTF-8"), ArrayList.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ��� �˾����� id �ҷ�����
		List<Integer> popupAdsList = messageService.getAllPopupId();
		log.info(popupAdsList);
		
		if(blockList == null) {
			return new ResponseEntity<List<Integer>>(popupAdsList, HttpStatus.OK);
		}
		
		// �ҷ��� �˾������� ���ܸ�� ����
		
		// popupAdsList : 12 7 6 5 4 2
		// blockList : 11 9 7 5 3 1
		// ������ ���� blockList�� ��, ������ ���� blockList + popupAdsList�� ��, ũ�� result�� �߰� + ���� popupAdsList�� ��
		// blockList�� ���̻� ������ ���� popupAdsList ���� result �߰� �� ����, popupAdsList�� ���̻� ������ ����
		
		// �������� ����
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
			if(blockIter.hasNext()) { // Ȯ���� block id�� �ִٸ� ������ ��
				blockId = blockIter.next();
				compare = popupAdsList.get(i) - blockId;
				if(compare > 0) { // ���� ū block id���� ũ�� ������ blockList�� �������� �ʴ� �� == ���ܵ��� ���� id
					resultList.add(popupAdsList.get(i++));
				}else if(compare == 0) { // ���ܵ� id �߽߰� �ش� id�� result�� �߰����� �ʰ� ���� popupAds Ȯ�� 
					i++;
				} // ���� ū popupAds id���� ū block id�� ��ŵ
			}else { // ��� block id�� �� Ȯ���ߴٸ�, ���� popupAds�� ���� result�� �߰�
				while(i < popupAdsList.size()) {
					resultList.add(popupAdsList.get(i++));
				}
				break;
			}
		}
	
		return new ResponseEntity<List<Integer>>(resultList, HttpStatus.OK);
	} // end blockPopup
	
	
}
