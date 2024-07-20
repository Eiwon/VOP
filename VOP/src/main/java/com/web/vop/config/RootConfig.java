package com.web.vop.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// root-context.xml과 동일
@Configuration
@ComponentScan(basePackages = {"com.web.vop.service", "com.web.vop.handler"})
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.web.vop.persistence"})
@EnableTransactionManagement // 트랜잭션 관리 활성화
public class RootConfig {
   
   @Bean // 스프링 bean으로 설정
   public DataSource dataSource() { // DataSource 객체 리턴 메서드
      HikariConfig config = new HikariConfig(); // 설정 객체
      config.setDriverClassName("oracle.jdbc.OracleDriver"); // jdbc 드라이버 정보
      config.setJdbcUrl("jdbc:oracle:thin:@vop-master.cd26qugoeupc.ap-northeast-2.rds.amazonaws.com:1521:orcl"); // DB 연결 url
      config.setUsername("vopmaster"); // DB 사용자 아이디
      config.setPassword("vopmaster"); // DB 사용자 비밀번호
      
      config.setMaximumPoolSize(20); // 최대 풀(Pool) 크기 설정
      config.setConnectionTimeout(30000); // Connection 타임 아웃 설정(30초)
      HikariDataSource ds = new HikariDataSource(config); // config 객체를 참조하여 DataSource 객체 생성
      return ds; // ds 객체 리턴
   }
   
   @Bean
   public SqlSessionFactory sqlSessionFactory() throws Exception { 
      SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
      sqlSessionFactoryBean.setDataSource(dataSource());
      return (SqlSessionFactory) sqlSessionFactoryBean.getObject();
   }
   
   // 트랜잭션 매니저 객체를 빈으로 등록
   @Bean
   public PlatformTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSource());
   }
   
   // 우리 DB에 자동로그인 토큰을 저장하도록 설정 (자동 로그인 테이블은 security에서 정해준 대로 만들어야함)
   @Bean
   public PersistentTokenRepository tokenRepository() {
	   JdbcTokenRepositoryImpl tokenRepositoryImple = new JdbcTokenRepositoryImpl();
	   tokenRepositoryImple.setDataSource(dataSource());
	   return tokenRepositoryImple;
   }
   
   // json <-> java object 변환용 객체
   @Bean
   public ObjectMapper objectMapper() {
	   return new ObjectMapper();
   } 
   
   
} // end RootConfig