package com.web.vop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	@Bean
	public TaskScheduler scheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler(); // Thread Pool 기반의 스케줄러
		scheduler.setPoolSize(4); // 스레드 풀 내의 스레드 4개 생성
		return scheduler;
	}
	
}//end SchedulerConfig
