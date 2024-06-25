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
	 
	//@Scheduled(cron = "0 */10 * * * ?") // 10 �п� �� �� ����(TEST)
	@Scheduled(cron = "0 0 12 * * ?") // ���� 12�ÿ� ����
	public void checkMembershipExpiry() {
			
			List<MembershipExpiryDTO> expiryInfoList = membershipMapper.selectExpiryDateBySchedulling();
	     
	     // ��ȸ�� �������� ������� ó�� ������ ����
	        if (expiryInfoList != null && !expiryInfoList.isEmpty()) {
	            Date today = new Date();
	            for(MembershipExpiryDTO expiryInfo : expiryInfoList) {
	            		Date expiryDate = expiryInfo.getExpiryDate();
	            	if (expiryDate.before(today)) {
	            		// ���� ó�� ���� �ۼ�
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate, expiryInfo.getMemberEmail());
	            	}else {
	                    // �����ϱ��� ���� �ϼ� ���
	                    long diffInMillies = Math.abs(expiryDate.getTime() - today.getTime());
	                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	                    System.out.println("-- ����� �˸� --");
	                    System.out.println("����� ID : " + expiryInfo.getMemberId() + ", �����ϱ��� ���� �ϼ�: " + diffInDays + "��");
	                }
	            }// end for()
	        }// if()
	}//end checkMembershipExpiry()

	@Scheduled(cron = "0 0 11 * * ?") // ���� 11�ÿ� �� �� ����(TEST)
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
                    // ������� ���� ��¥�� ���� ���: ��� �Ϸ� �˸�
                    long daysSinceDelivery = TimeUnit.DAYS.convert(today2.getTime() - expectDate.getTime(), TimeUnit.MILLISECONDS);
                    if (daysSinceDelivery <= 7) {
                        // ���÷κ��� ������ �̳��� ��� �Ϸ� �ǿ� ���ؼ��� �˸�
                        sendDeliveryCompleteNotification(list.getMemberId(),list.getOrderId(), list.getProductId(), expectDate, list.getMemberEmail());
                    }
                } else if (diffInDays == 0) {
                    // ������� ���� ��¥�� ���� ���: ���� ��� �޽���
                	title = "-- ���� ��� �˸� --";
                	content = "����� ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", ��ǰ ID : " + list.getProductId() + ", ���� ��۵˴ϴ�.";
                	
            		// �̸��� �۽�
            		mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                } else {
                    // ������� ���� ��¥�� ������ ���� ���: ��� ���� �˸�
                	title = "-- ��� ������ �˸� --";
                	content = "����� ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", ��ǰ ID : " + list.getProductId() + ", " + diffInDays + "�� �� ��ۿϷ� �˴ϴ�.";
                	
            		// �̸��� �۽�
            		mailAuthenticationUtil.sendEmail(list.getMemberEmail(), title, content);
                	System.out.println(title);
                    System.out.println(content);
                }
				}// end for()
			}// if()
		}// end checkDeliveryExpiry() 

	
	
	private void sendDeliveryCompleteNotification(String memberId,int orderId, int productId, Date expectDate, String memberEmail) {
		// ��ۿϷ� �˸��̳� �̸����� ������ ���� ����
		String title = "-- ��� �Ϸ� �˸� --";
		String content = "����� Id : " + memberId + ", Order ID : " + orderId + ", ��ǰ ID : " + productId + ", ����� �Ϸ�Ǿ����ϴ�. ��۳�¥ : " + expectDate;
		
		// �̸��� �۽�
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpectNotification()


	private void sendExpiryNotification(String memberId, Date expiryDate, String memberEmail) {
		// �˸��̳� �̸����� ������ ���� ���� 
		String title = "-- ����� �˸� --";
		String content = "����� ID : " + memberId + ", ������� ����Ǿ����ϴ�. ����� ���� ��¥�� " + expiryDate + "�Դϴ�.";
		
		mailAuthenticationUtil.sendEmail(memberEmail, title, content);
		
		System.out.println(title);
		System.out.println(content);
	}//end sendExpiryNotification()
	
	
	
	
}//end SchedulerServiceImple
