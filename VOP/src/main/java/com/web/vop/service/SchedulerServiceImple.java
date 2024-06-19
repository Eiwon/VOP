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


	private void sendExpiryNotification(String memberId, Date expiryDate) {
		// �˸��̳� �̸����� ������ ���� ���� 
		System.out.println("-- ����� �˸� --");
		System.out.println("����� ID : " + memberId + ", ������� ����Ǿ����ϴ�. ����� ���� ��¥�� " + expiryDate + "�Դϴ�.");
	}//end sendExpiryNotification()
	
	
	
	
}//end SchedulerServiceImple
