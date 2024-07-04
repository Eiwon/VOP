package com.web.vop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@EnableWebMvc
public class S3Config {
	
	// S3 접근 권한 인증을 위해 발급받은 key 값
	private String accessKey = ApiKey.awsAccessKey;//
	private String secretKey = ApiKey.awsSecretKey;//
	
	@Bean
	public AmazonS3 awsS3Client() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3 s3Client = 
				AmazonS3ClientBuilder
				.standard()
				.withRegion(Regions.AP_NORTHEAST_2) // s3가 등록된 지역 코드 설정 
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.build();
		return s3Client;
	} // end awsS3Client
	
	// s3 저장소에 접근하기 위한 url
	@Bean
	public String s3Url() {
		return "https://vop-s3-bucket.s3.ap-northeast-2.amazonaws.com/";
	} // end s3Url
	
	// AWS Cloud Front URL
	@Bean
	public String cloudFrontUrl() {
		return "https://d2j7x0t84z88uc.cloudfront.net/";
	} // end cloudFrontUrl
	
}
