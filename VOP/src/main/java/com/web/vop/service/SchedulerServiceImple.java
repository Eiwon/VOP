package com.web.vop.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.web.vop.domain.MembershipExpiryDTO;
import com.web.vop.persistence.MembershipMapper;


@Component
public class SchedulerServiceImple {

	 @Autowired
	 MembershipMapper membershipMapper;

	 
	//@Scheduled(cron = "0 */10 * * * ?") // 10 분에 한 번 실행(TEST)
	@Scheduled(cron = "0 0 12 * * ?") // 매일 12시에 실행
	public void checkMembershipExpiry() {
			
			List<MembershipExpiryDTO> expiryInfoList = membershipMapper.selectExpiryDateBySchedulling();
	     
	     // 조회된 만료일을 기반으로 처리 로직을 구현
	        if (expiryInfoList != null && !expiryInfoList.isEmpty()) {
	            Date today = new Date();
	            for(MembershipExpiryDTO expiryInfo : expiryInfoList) {
	            		Date expiryDate = expiryInfo.getExpiryDate();
	            	if (expiryDate.before(today)) {
	            		// 만료 처리 로직 작성
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate);
	            	}else {
	                    // 만료일까지 남은 일수 계산
	                    long diffInMillies = Math.abs(expiryDate.getTime() - today.getTime());
	                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	                    System.out.println("-- 멤버십 알림 --");
	                    System.out.println("사용자 ID : " + expiryInfo.getMemberId() + ", 만료일까지 남은 일수: " + diffInDays + "일");
	                }
	            }// end for()
	        }// if()
	}//end checkMembershipExpiry()


	private void sendExpiryNotification(String memberId, Date expiryDate) {
		// 알림이나 이메일을 보내는 로직 구현 
		System.out.println("-- 멤버십 알림 --");
		System.out.println("사용자 ID : " + memberId + ", 멤버십이 만료되었습니다. 멤버십 만료 날짜는 " + expiryDate + "입니다.");
	}//end sendExpiryNotification()
	
	
	
	
}//end SchedulerServiceImple
