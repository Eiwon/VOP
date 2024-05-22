package com.web.vop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.vop.domain.AnswerVO;
import com.web.vop.service.AnswerService;
import com.web.vop.service.ProductService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/answer")
@Log4j
public class AnswerController {
	
	@Autowired
	private AnswerService answerService;
	
	
	
}
