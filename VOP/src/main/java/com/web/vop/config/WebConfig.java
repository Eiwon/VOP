package com.web.vop.config;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// web.xml�� ����
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer{

   // root application context(Root WebApplicationContext)�� �����ϴ� ���� Ŭ���� ���� �޼��� 
   @Override
   protected Class<?>[] getRootConfigClasses() {
      
      return new Class[] {
    		  RootConfig.class, SecurityConfig.class, S3Config.class, PaymentAPIConfig.class}; // RootConfig Ŭ���� ����
   }

   // servlet application context(Servlet WebApplicationContext)�� �����ϴ� ���� Ŭ���� ���� �޼���
   @Override
   protected Class<?>[] getServletConfigClasses() {
      
      return new Class[] {ServletConfig.class, WebSocketConfig.class}; // ServletConfig Ŭ���� ����
   }

   // Servlet Mapping �޼���
   @Override
   protected String[] getServletMappings() {
      
      return new String[] {"/"}; // �⺻ ��� ����
   }
   
   @Override
   protected void customizeRegistration(Dynamic registration) {
	   // 404 not found�� ���ܰ� �ƴ� => �߻��� ���ܸ� �������� ����
	   registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
   } // end customizeRegistration

} // end WebConfig




