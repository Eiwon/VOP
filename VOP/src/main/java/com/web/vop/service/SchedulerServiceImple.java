package com.web.vop.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.domain.DeliveryExpectDTO;
import com.web.vop.domain.MembershipExpiryDTO;
import com.web.vop.persistence.MembershipMapper;
import com.web.vop.persistence.OrderMapper;
import com.web.vop.socket.AlarmHandler;
import com.web.vop.util.MailAuthenticationUtil;


@Component
public class SchedulerServiceImple {

	 @Autowired
	 MembershipMapper membershipMapper;

	 @Autowired
	 OrderMapper orderMapper;
	 
	 @Autowired
	 MailAuthenticationUtil mailAuthenticationUtil;
	 
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
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate, expiryInfo.getMemberEmail());
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

	@Scheduled(cron = "0 0 11 * * ?") // 매일 11시에 한 번 실행(TEST)
	public void checkDeliveryExpiry() {
		String title, content;
		List<DeliveryExpectDTO> expectInfoList = orderMapper.selectDeliveryExpect();
			Date today2 = new Date();
		if(expectInfoList != null && !expectInfoList.isEmpty()) {
			for(DeliveryExpectDTO list : expectInfoList) {
				Date expectDate = list.getExpectDeliveryDate();
				long diffInMillies = expectDate.getTime() - today2.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                
                if (diffInDays < 0) {
                    // 배송일이 현재 날짜를 지난 경우: 배송 완료 알림
                    long daysSinceDelivery = TimeUnit.DAYS.convert(today2.getTime() - expectDate.getTime(), TimeUnit.MILLISECONDS);
                    if (daysSinceDelivery <= 7) {
                        // 오늘로부터 일주일 이내의 배송 완료 건에 대해서만 알림
                        sendDeliveryCompleteNotification(list.getMemberId(),list.getOrderId(), list.getProductId(), expectDate, list.getMemberEmail());
                    }
                } else if (diffInDays == 0) {
                    // 배송일이 현재 날짜와 같은 경우: 당일 배송 메시지
                	title = "-- 당일 배송 알림 --";
                	content = "사용자 ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", 상품 ID : " + list.getProductId() + ", 오늘 배송됩니다.";
                	
            		// 이메일 송신
            		mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                } else {
                    // 배송일이 현재 날짜를 지나지 않은 경우: 배송 예정 알림
                	title = "-- 배송 예정일 알림 --";
                	content = "사용자 ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", 상품 ID : " + list.getProductId() + ", " + diffInDays + "일 후 배송완료 됩니다.";
                	
            		// 이메일 송신
            		mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                }
				}// end for()
			}// if()
		}// end checkDeliveryExpiry() 

	
	
	private void sendDeliveryCompleteNotification(String memberId,int orderId, int productId, Date expectDate, String memberEmail) {
		// 배송완료 알림이나 이메일을 보내는 로직 구현
		String title = "-- 배송 완료 알림 --";
		String content = "사용자 Id : " + memberId + ", Order ID : " + orderId + ", 상품 ID : " + productId + ", 배송이 완료되었습니다. 배송날짜 : " + expectDate;
		
		// 이메일 송신
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpectNotification()


	private void sendExpiryNotification(String memberId, Date expiryDate, String memberEmail) {
		// 알림이나 이메일을 보내는 로직 구현 
		String title = "-- 멤버십 알림 --";
		String content = "사용자 ID : " + memberId + ", 멤버십이 만료되었습니다. 멤버십 만료 날짜는 " + expiryDate + "입니다.";
		
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpiryNotification()
	
	
	
	
}//end SchedulerServiceImple
