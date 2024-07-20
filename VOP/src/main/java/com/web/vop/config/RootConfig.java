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

// root-context.xml�� ����
@Configuration
@ComponentScan(basePackages = {"com.web.vop.service", "com.web.vop.handler"})
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.web.vop.persistence"})
@EnableTransactionManagement // Ʈ����� ���� Ȱ��ȭ
public class RootConfig {
   
   @Bean // ������ bean���� ����
   public DataSource dataSource() { // DataSource ��ü ���� �޼���
      HikariConfig config = new HikariConfig(); // ���� ��ü
      config.setDriverClassName("oracle.jdbc.OracleDriver"); // jdbc ����̹� ����
      config.setJdbcUrl("jdbc:oracle:thin:@vop-master.cd26qugoeupc.ap-northeast-2.rds.amazonaws.com:1521:orcl"); // DB ���� url
      config.setUsername("vopmaster"); // DB ����� ���̵�
      config.setPassword("vopmaster"); // DB ����� ��й�ȣ
      
      config.setMaximumPoolSize(20); // �ִ� Ǯ(Pool) ũ�� ����
      config.setConnectionTimeout(30000); // Connection Ÿ�� �ƿ� ����(30��)
      HikariDataSource ds = new HikariDataSource(config); // config ��ü�� �����Ͽ� DataSource ��ü ����
      return ds; // ds ��ü ����
   }
   
   @Bean
   public SqlSessionFactory sqlSessionFactory() throws Exception { 
      SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
      sqlSessionFactoryBean.setDataSource(dataSource());
      return (SqlSessionFactory) sqlSessionFactoryBean.getObject();
   }
   
   // Ʈ����� �Ŵ��� ��ü�� ������ ���
   @Bean
   public PlatformTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSource());
   }
   
   // �츮 DB�� �ڵ��α��� ��ū�� �����ϵ��� ���� (�ڵ� �α��� ���̺��� security���� ������ ��� ��������)
   @Bean
   public PersistentTokenRepository tokenRepository() {
	   JdbcTokenRepositoryImpl tokenRepositoryImple = new JdbcTokenRepositoryImpl();
	   tokenRepositoryImple.setDataSource(dataSource());
	   return tokenRepositoryImple;
   }
   
   // json <-> java object ��ȯ�� ��ü
   @Bean
   public ObjectMapper objectMapper() {
	   return new ObjectMapper();
   } 
   
   
} // end RootConfig