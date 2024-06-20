package com.web.vop.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.web.vop.domain.DeliveryExpectDTO;
import com.web.vop.domain.MembershipExpiryDTO;
import com.web.vop.persistence.MembershipMapper;
import com.web.vop.persistence.OrderMapper;


@Component
public class SchedulerServiceImple {

	 @Autowired
	 MembershipMapper membershipMapper;

	 @Autowired
	 OrderMapper orderMapper;
	 
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
	            		sendExpiryNotification(expiryInfo.getMemberId(),expiryDate);
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

	//@Scheduled(cron = "0 * * * * ?") // 1 �п� �� �� ����(TEST)
	public void checkDeliveryExpiry() {
		
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
                        sendDeliveryCompleteNotification(list.getMemberId(),list.getOrderId(), list.getProductId(), expectDate);
                    }
                } else if (diffInDays == 0) {
                    // ������� ���� ��¥�� ���� ���: ���� ��� �޽���
                    System.out.println("-- ���� ��� �˸� --");
                    System.out.println("����� ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", ��ǰ ID : " + list.getProductId() + ", ���� ��۵˴ϴ�.");
                } else {
                    // ������� ���� ��¥�� ������ ���� ���: ��� ���� �˸�
                    System.out.println("-- ��� ������ �˸� --");
                    System.out.println("����� ID : " + list.getMemberId() + ", OrderId : " + list.getOrderId() + ", ��ǰ ID : " + list.getProductId() + ", " + diffInDays + "�� �� ��ۿϷ� �˴ϴ�.");
                }
				}// end for()
			}// if()
		}// end checkDeliveryExpiry() 

	
	
	private void sendDeliveryCompleteNotification(String memberId,int orderId, int productId, Date expectDate) {
		// ��ۿϷ� �˸��̳� �̸����� ������ ���� ����
		System.out.println("-- ��� �Ϸ� �˸� --");
		System.out.println("����� Id : " + memberId + ", Order ID : " + orderId + ", ��ǰ ID : " + productId + ", ����� �Ϸ�Ǿ����ϴ�. ��۳�¥ : " + expectDate);
	}//end sendExpectNotification()


	private void sendExpiryNotification(String memberId, Date expiryDate) {
		// �˸��̳� �̸����� ������ ���� ���� 
		System.out.println("-- ����� �˸� --");
		System.out.println("����� ID : " + memberId + ", ������� ����Ǿ����ϴ�. ����� ���� ��¥�� " + expiryDate + "�Դϴ�.");
	}//end sendExpiryNotification()
	
	
	
	
}//end SchedulerServiceImple
