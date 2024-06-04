package com.web.vop.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerServiceImple {

	@Scheduled(fixedDelay = 3000)
	public void run() {
		//System.out.println("gogo");
	}
	
}//end SchedulerServiceImple
