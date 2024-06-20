package com.web.vop.config;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// web.xml과 동일
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer{

   // root application context(Root WebApplicationContext)에 적용하는 설정 클래스 지정 메서드 
   @Override
   protected Class<?>[] getRootConfigClasses() {
      
      return new Class[] {
    		  RootConfig.class, SecurityConfig.class, S3Config.class, PaymentAPIConfig.class}; // RootConfig 클래스 리턴
   }

   // servlet application context(Servlet WebApplicationContext)에 적용하는 설정 클래스 지정 메서드
   @Override
   protected Class<?>[] getServletConfigClasses() {
      
      return new Class[] {ServletConfig.class, WebSocketConfig.class}; // ServletConfig 클래스 리턴
   }

   // Servlet Mapping 메서드
   @Override
   protected String[] getServletMappings() {
      
      return new String[] {"/"}; // 기본 경로 리턴
   }
   
   @Override
   protected void customizeRegistration(Dynamic registration) {
	   // 404 not found는 예외가 아님 => 발생시 예외를 던지도록 설정
	   registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
   } // end customizeRegistration

} // end WebConfig




