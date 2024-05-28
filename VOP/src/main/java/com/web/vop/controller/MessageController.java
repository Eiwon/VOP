package com.web.vop.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class MessageController {

	@RequestMapping(value = "/msg")
	public String test(String msg) {
		log.info(msg);
		return msg;
	}
	
}
