package com.web.vop.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;

import com.web.vop.domain.DeliveryExpectDTO;
import com.web.vop.domain.DeliveryMsg;
import com.web.vop.domain.MembershipExpiryDTO;
import com.web.vop.persistence.MembershipMapper;
import com.web.vop.persistence.OrderMapper;
import com.web.vop.socket.AlarmHandler;
import com.web.vop.util.MailAuthenticationUtil;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class SchedulerServiceImple {

	 @Autowired
	 MembershipMapper membershipMapper;

	 @Autowired
	 OrderMapper orderMapper;
	 
	 @Autowired
	 MailAuthenticationUtil mailAuthenticationUtil;
	 
	 // key : memberEmail, value : 보낼 메시지, memberEmail당 하나의 메일만 보내기 위함
	 Map<String, DeliveryMsg> deliveryMsgMap;
	 
	//@Scheduled(cron = "0 * * * * ?") // 1 분에 한 번 실행(TEST)
	@Scheduled(cron = "0 0 14 * * ?") // 매일 14시에 실행
	public void checkMembershipExpiry() {
			
			List<MembershipExpiryDTO> expiryInfoList = membershipMapper.selectExpiryDateBySchedulling();
	     log.info(expiryInfoList);
	     // 조회된 만료일을 기반으로 처리 로직을 구현
	        if (expiryInfoList != null && !expiryInfoList.isEmpty()) {
	            Date today = new Date();
	            for(MembershipExpiryDTO expiryInfo : expiryInfoList) {
	            		Date expiryDate = expiryInfo.getExpiryDate();
	            		long diffInMillies = expiryDate.getTime() - today.getTime();
	                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	                    
	                if (diffInDays == 0) {
	                	// 오늘이 만료일인 경우
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate, expiryInfo.getMemberEmail(),"멤버십이 당일 만료됩니다.");
	                }else if (diffInDays < 0) {
	            		// 만료일이 지난 경우
	                	long daysSinceDelivery = TimeUnit.DAYS.convert(today.getTime() - expiryDate.getTime(), TimeUnit.MILLISECONDS);
	                    if (daysSinceDelivery <= 5) {
	                        // 멤버십이 만료된 지, 5일 이내의 회원에게만 메세지 보내기
	                    	 handleExpiredMembership(expiryInfo.getMemberId(), expiryDate, expiryInfo.getMemberEmail());
	                    }
	            	}else if (diffInDays < 3){
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate, expiryInfo.getMemberEmail(),"멤버십이 곧 만료됩니다!");   
	                }
	            }// end for()
	        }// if()
	}//end checkMembershipExpiry()
	
	

	//@Scheduled(cron = "0 * * * * ?") // 1 분에 한 번 실행(TEST)
	@Scheduled(cron = "0 0 13 * * ?") // 매일 13시에 한 번 실행(TEST)
	public void checkDeliveryExpiry() {
		deliveryMsgMap = new HashMap<>();
		String title, content;
		List<DeliveryExpectDTO> expectInfoList = orderMapper.selectDeliveryExpect();
		Date today2 = new Date();
		if(expectInfoList != null && !expectInfoList.isEmpty()) {
			for(DeliveryExpectDTO list : expectInfoList) {
				Date expectDate = list.getExpectDeliveryDate();
				long diffInMillies = expectDate.getTime() - today2.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                
                // 이메일을 맵에 등록
                deliveryMsgMap.putIfAbsent(list.getMemberEmail(), new DeliveryMsg());
                DeliveryMsg dm = deliveryMsgMap.get(list.getMemberEmail());
                
                if (diffInDays < 0) {
                    // 배송일이 현재 날짜를 지난 경우: 배송 완료 알림
                    long daysSinceDelivery = TimeUnit.DAYS.convert(today2.getTime() - expectDate.getTime(), TimeUnit.MILLISECONDS);
                    if (daysSinceDelivery <= 7) {
                        // 오늘로부터 일주일 이내의 배송 완료 건에 대해서만 알림
                    	dm.addCompleteMsg("사용자 ID : " + list.getMemberId() + ", Order ID : " + list.getOrderId() + ", 상품 ID : " + list.getProductId() + ", 배송이 완료되었습니다. 배송날짜 : " + list.getExpectDeliveryDate());
                        //sendDeliveryCompleteNotification(list.getMemberId(),list.getOrderId(), list.getProductId(), expectDate, list.getMemberEmail());
                    }
                } else if (diffInDays == 0) {
                    // 배송일이 현재 날짜와 같은 경우: 당일 배송 메시지
                	title = "-- 당일 배송 알림 --";
                	content = "사용자 ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", 상품 ID : " + list.getProductId() + ", 오늘 배송됩니다.";
                	dm.addTodayMsg(content);
            		// 이메일 송신
            		//mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                } else {
                    // 배송일이 현재 날짜를 지나지 않은 경우: 배송 예정 알림
                	title = "-- 배송 예정일 알림 --";
                	content = "사용자 ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", 상품 ID : " + list.getProductId() + ", " + diffInDays + "일 후 배송완료 됩니다.";
                	dm.addExpectMsg(content);
            		// 이메일 송신
            		//mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                }
				}// end for()
			}// if()
		
			for(String memberEmail : deliveryMsgMap.keySet()) {
				mailAuthenticationUtil.sendEmail(memberEmail, "VOP 배송 알림", deliveryMsgMap.get(memberEmail).getContent());
			}
		}// end checkDeliveryExpiry() 

	
	// 만료된 멤버십을 처리 (재등록 요청 메일)
	private void handleExpiredMembership(String memberId, Date expiryDate, String memberEmail) {
		if (memberEmail == null || memberEmail.isEmpty()) {
            log.error("Email address is null or empty for memberId: " + memberId);
            return;
        }
		
		String title = "-- 멤버십 재등록 알림 --";
		String content = memberId + "님, 멤버십이 만료되었습니다. 재등록이 필요합니다. ( 만료 날짜 : " + expiryDate + " )";
		
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end handleExpiredMembership()
		
	private void sendDeliveryCompleteNotification(String memberId,int orderId, int productId, Date expectDate, String memberEmail) {
		// 배송완료 알림이나 이메일을 보내는 로직 구현
		String title = "-- 배송 완료 알림 --";
		String content = "사용자 Id : " + memberId + ", Order ID : " + orderId + ", 상품 ID : " + productId + ", 배송이 완료되었습니다. 배송날짜 : " + expectDate;
		
		// 이메일 송신
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpectNotification()


	private void sendExpiryNotification(String memberId, Date expiryDate, String memberEmail,String message) {
		
		if (memberEmail == null || memberEmail.isEmpty()) {
            log.error("Email address is null or empty for memberId: " + memberId);
            return;
        }
		
		String title = "-- 멤버십 알림 --";
		String content = "사용자 ID : " + memberId + "님," + message + " 멤버십 만료 날짜는 " + expiryDate + "입니다.";
		
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpiryNotification()
}//end SchedulerServiceImple
