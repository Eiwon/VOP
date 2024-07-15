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
	 
	 // key : memberEmail, value : ���� �޽���, memberEmail�� �ϳ��� ���ϸ� ������ ����
	 Map<String, DeliveryMsg> deliveryMsgMap;
	 
	//@Scheduled(cron = "0 * * * * ?") // 1 �п� �� �� ����(TEST)
	@Scheduled(cron = "0 0 14 * * ?") // ���� 14�ÿ� ����
	public void checkMembershipExpiry() {
			
			List<MembershipExpiryDTO> expiryInfoList = membershipMapper.selectExpiryDateBySchedulling();
	     log.info(expiryInfoList);
	     // ��ȸ�� �������� ������� ó�� ������ ����
	        if (expiryInfoList != null && !expiryInfoList.isEmpty()) {
	            Date today = new Date();
	            for(MembershipExpiryDTO expiryInfo : expiryInfoList) {
	            		Date expiryDate = expiryInfo.getExpiryDate();
	            		long diffInMillies = expiryDate.getTime() - today.getTime();
	                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	                    
	                if (diffInDays == 0) {
	                	// ������ �������� ���
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate, expiryInfo.getMemberEmail(),"������� ���� ����˴ϴ�.");
	                }else if (diffInDays < 0) {
	            		// �������� ���� ���
	                	long daysSinceDelivery = TimeUnit.DAYS.convert(today.getTime() - expiryDate.getTime(), TimeUnit.MILLISECONDS);
	                    if (daysSinceDelivery <= 5) {
	                        // ������� ����� ��, 5�� �̳��� ȸ�����Ը� �޼��� ������
	                    	 handleExpiredMembership(expiryInfo.getMemberId(), expiryDate, expiryInfo.getMemberEmail());
	                    }
	            	}else if (diffInDays < 3){
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate, expiryInfo.getMemberEmail(),"������� �� ����˴ϴ�!");   
	                }
	            }// end for()
	        }// if()
	}//end checkMembershipExpiry()
	
	

	//@Scheduled(cron = "0 * * * * ?") // 1 �п� �� �� ����(TEST)
	@Scheduled(cron = "0 0 13 * * ?") // ���� 13�ÿ� �� �� ����(TEST)
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
                
                // �̸����� �ʿ� ���
                deliveryMsgMap.putIfAbsent(list.getMemberEmail(), new DeliveryMsg());
                DeliveryMsg dm = deliveryMsgMap.get(list.getMemberEmail());
                
                if (diffInDays < 0) {
                    // ������� ���� ��¥�� ���� ���: ��� �Ϸ� �˸�
                    long daysSinceDelivery = TimeUnit.DAYS.convert(today2.getTime() - expectDate.getTime(), TimeUnit.MILLISECONDS);
                    if (daysSinceDelivery <= 7) {
                        // ���÷κ��� ������ �̳��� ��� �Ϸ� �ǿ� ���ؼ��� �˸�
                    	dm.addCompleteMsg("����� ID : " + list.getMemberId() + ", Order ID : " + list.getOrderId() + ", ��ǰ ID : " + list.getProductId() + ", ����� �Ϸ�Ǿ����ϴ�. ��۳�¥ : " + list.getExpectDeliveryDate());
                        //sendDeliveryCompleteNotification(list.getMemberId(),list.getOrderId(), list.getProductId(), expectDate, list.getMemberEmail());
                    }
                } else if (diffInDays == 0) {
                    // ������� ���� ��¥�� ���� ���: ���� ��� �޽���
                	title = "-- ���� ��� �˸� --";
                	content = "����� ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", ��ǰ ID : " + list.getProductId() + ", ���� ��۵˴ϴ�.";
                	dm.addTodayMsg(content);
            		// �̸��� �۽�
            		//mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                } else {
                    // ������� ���� ��¥�� ������ ���� ���: ��� ���� �˸�
                	title = "-- ��� ������ �˸� --";
                	content = "����� ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", ��ǰ ID : " + list.getProductId() + ", " + diffInDays + "�� �� ��ۿϷ� �˴ϴ�.";
                	dm.addExpectMsg(content);
            		// �̸��� �۽�
            		//mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                }
				}// end for()
			}// if()
		
			for(String memberEmail : deliveryMsgMap.keySet()) {
				mailAuthenticationUtil.sendEmail(memberEmail, "VOP ��� �˸�", deliveryMsgMap.get(memberEmail).getContent());
			}
		}// end checkDeliveryExpiry() 

	
	// ����� ������� ó�� (���� ��û ����)
	private void handleExpiredMembership(String memberId, Date expiryDate, String memberEmail) {
		if (memberEmail == null || memberEmail.isEmpty()) {
            log.error("Email address is null or empty for memberId: " + memberId);
            return;
        }
		
		String title = "-- ����� ���� �˸� --";
		String content = memberId + "��, ������� ����Ǿ����ϴ�. ������ �ʿ��մϴ�. ( ���� ��¥ : " + expiryDate + " )";
		
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end handleExpiredMembership()
		
	private void sendDeliveryCompleteNotification(String memberId,int orderId, int productId, Date expectDate, String memberEmail) {
		// ��ۿϷ� �˸��̳� �̸����� ������ ���� ����
		String title = "-- ��� �Ϸ� �˸� --";
		String content = "����� Id : " + memberId + ", Order ID : " + orderId + ", ��ǰ ID : " + productId + ", ����� �Ϸ�Ǿ����ϴ�. ��۳�¥ : " + expectDate;
		
		// �̸��� �۽�
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpectNotification()


	private void sendExpiryNotification(String memberId, Date expiryDate, String memberEmail,String message) {
		
		if (memberEmail == null || memberEmail.isEmpty()) {
            log.error("Email address is null or empty for memberId: " + memberId);
            return;
        }
		
		String title = "-- ����� �˸� --";
		String content = "����� ID : " + memberId + "��," + message + " ����� ���� ��¥�� " + expiryDate + "�Դϴ�.";
		
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpiryNotification()
}//end SchedulerServiceImple
