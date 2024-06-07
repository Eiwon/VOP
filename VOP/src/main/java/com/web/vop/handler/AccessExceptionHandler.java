package com.web.vop.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.log4j.Log4j;

@ControllerAdvice
@Log4j
public class AccessExceptionHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
	public String NoHandlerFoundExceptionHandler() {
		log.error("NoHandlerFoundException");
		return "redirect:/access/notFound";
	} // end NoHandlerFoundExceptionHandler
	
	@ExceptionHandler(Exception.class)
	public String ExceptionHandler(Exception exception) {
		log.error("예외 발생!! : " + exception);
		return "redirect:/access/exception";
	}
	
}
