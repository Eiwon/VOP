package com.web.vop.domain;

// 스케줄러로 보낼 이메일 내용을 생성하는 객체
public class DeliveryMsg {

	private String deliveryExpectMsg = null;
	private String deliveryCompleteMsg = null;
	private String deliveryTodayMsg = null;
	
	public void addExpectMsg(String msg) {
		if(deliveryExpectMsg == null) {
			deliveryExpectMsg = "-- 배송 예정일 알림 --\r\n";
		}
		deliveryExpectMsg = deliveryExpectMsg.concat(msg + "\r\n");
	}
	
	public void addCompleteMsg(String msg) {
		if(deliveryCompleteMsg == null) {
			deliveryCompleteMsg = "-- 배송 완료 알림 --\r\n";
		}
		deliveryCompleteMsg = deliveryCompleteMsg.concat(msg + "\r\n");
	}
	
	public void addTodayMsg(String msg) {
		if(deliveryTodayMsg == null) {
			deliveryTodayMsg = "-- 당일 배송 알림 --\r\n";
		}
		deliveryTodayMsg = deliveryTodayMsg.concat(msg + "\r\n");
	}
	
	public String getContent() {
		String msg = "";
		
		if(deliveryExpectMsg != null) {
			msg = msg.concat(deliveryExpectMsg + "\r\n");
		}
		if(deliveryTodayMsg != null) {
			msg = msg.concat(deliveryTodayMsg + "\r\n");
		}
		if(deliveryCompleteMsg != null) {
			msg = msg.concat(deliveryCompleteMsg);
		}
		return msg;
	}
}
