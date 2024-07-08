package com.web.vop.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

// servlet-context.xml�� ���� 
@Configuration // Spring Container���� �����ϴ� ���� Ŭ����
@EnableWebMvc // Spring MVC ��� ���
@ComponentScan(basePackages = { "com.web.vop", "com.web.vop.socket"}) // component scan ����

public class ServletConfig implements WebMvcConfigurer {

   // ViewResolver ���� �޼���
   @Override
   public void configureViewResolvers(ViewResolverRegistry registry) {
      // InternalResourceViewResolver ���� �� ����
      InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
      viewResolver.setPrefix("/WEB-INF/views/");
      viewResolver.setSuffix(".jsp");
      registry.viewResolver(viewResolver);
   }

   // ResourceHandlers ���� �޼���
   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      // resources ���丮 ����
      registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
   }
   
   // ������ ������ ��� bean ����
   @Bean
   public String uploadPath() { // ��ǰ ������ �̹��� ���� ���
      //return "C:\\upload\\ex04";
	  return "productDetails/";
   }
   
   @Bean
   public String thumbnailUploadPath() { // ��ǰ ����� �̹��� ���� ���
	  return "productThumbnail/";
   }
   
   // MultipartResolver bean ����
   @Bean
   public CommonsMultipartResolver multipartResolver() {
      CommonsMultipartResolver resolver = new CommonsMultipartResolver();
      final int MB = 1048576;
      
      // Ŭ���̾�Ʈ�� ���ε��ϴ� ��û�� ��ü ũ�� (bytes)
      resolver.setMaxUploadSize(30 * MB); // 30MB

      // Ŭ���̾�Ʈ�� ���ε��ϴ� �� ������ �ִ� ũ�� (bytes)
      resolver.setMaxUploadSizePerFile(10 * MB); // 10MB

      return resolver;
   }
   
   
} // end ServletConfig
