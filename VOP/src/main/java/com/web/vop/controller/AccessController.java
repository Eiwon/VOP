package com.web.vop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.AlertVO;
import com.web.vop.persistence.Constant;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/access")
@Log4j
public class AccessController {
	
	
	@GetMapping("/denied")
	public void accessDeniedGET() {
		log.info("액세스 거부");
	} // end accessDeniedGET
	
	@GetMapping("/notFound")
	public String NoHandlerFoundException(Model model) {
		log.info("NoHandlerFoundException");
		AlertVO alertVO = new AlertVO();
		alertVO.setAlertMsg("존재하지 않는 페이지입니다.");
		alertVO.setRedirectUri("board/main");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end NoHandlerFoundException
	
	@GetMapping("/exception")
	public String allException(Model model, Exception exception) {
		log.info("allException");
		AlertVO alertVO = new AlertVO();
		alertVO.setAlertMsg(exception.toString() + " 예외 발생!!!!!!!");
		alertVO.setRedirectUri("board/main");
		model.addAttribute("alertVO", alertVO);
		return Constant.ALERT_PATH;
	} // end allException
	
}
