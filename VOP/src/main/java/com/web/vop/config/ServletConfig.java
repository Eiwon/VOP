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

// servlet-context.xml과 동일 
@Configuration // Spring Container에서 관리하는 설정 클래스
@EnableWebMvc // Spring MVC 기능 사용
@ComponentScan(basePackages = { "com.web.vop", "com.web.vop.socket"}) // component scan 설정

public class ServletConfig implements WebMvcConfigurer {

   // ViewResolver 설정 메서드
   @Override
   public void configureViewResolvers(ViewResolverRegistry registry) {
      // InternalResourceViewResolver 생성 및 설정
      InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
      viewResolver.setPrefix("/WEB-INF/views/");
      viewResolver.setSuffix(".jsp");
      registry.viewResolver(viewResolver);
   }

   // ResourceHandlers 설정 메서드
   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      // resources 디렉토리 설정
      registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
   }
   
   // 파일을 저장할 경로 bean 생성
   @Bean
   public String uploadPath() { // 상품 상세정보 이미지 저장 경로
      //return "C:\\upload\\ex04";
	  return "productDetails/";
   }
   
   @Bean
   public String thumbnailUploadPath() { // 상품 썸네일 이미지 저장 경로
	  return "productThumbnail/";
   }
   
   // MultipartResolver bean 생성
   @Bean
   public CommonsMultipartResolver multipartResolver() {
      CommonsMultipartResolver resolver = new CommonsMultipartResolver();
      final int MB = 1048576;
      
      // 클라이언트가 업로드하는 요청의 전체 크기 (bytes)
      resolver.setMaxUploadSize(30 * MB); // 30MB

      // 클라이언트가 업로드하는 각 파일의 최대 크기 (bytes)
      resolver.setMaxUploadSizePerFile(10 * MB); // 10MB

      return resolver;
   }
   
   
} // end ServletConfig
