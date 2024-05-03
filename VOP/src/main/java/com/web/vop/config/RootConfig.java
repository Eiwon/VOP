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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// root-context.xml과 동일
@Configuration
@ComponentScan(basePackages = {"com.web.vop.service"})
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.web.vop.persistence"})
@EnableTransactionManagement // 트랜잭션 관리 활성화
public class RootConfig {
   
   @Bean // 스프링 bean으로 설정
   public DataSource dataSource() { // DataSource 객체 리턴 메서드
      HikariConfig config = new HikariConfig(); // 설정 객체
      config.setDriverClassName("oracle.jdbc.OracleDriver"); // jdbc 드라이버 정보
      config.setJdbcUrl("jdbc:oracle:thin:@192.168.0.146:1521:xe"); // DB 연결 url
      config.setUsername("VOP"); // DB 사용자 아이디
      config.setPassword("VOP"); // DB 사용자 비밀번호
      
      config.setMaximumPoolSize(10); // 최대 풀(Pool) 크기 설정
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
} // end RootConfig