package com.web.vop.domain;

// �����ٷ��� ���� �̸��� ������ �����ϴ� ��ü
public class DeliveryMsg {

	private String deliveryExpectMsg = null;
	private String deliveryCompleteMsg = null;
	private String deliveryTodayMsg = null;
	
	public void addExpectMsg(String msg) {
		if(deliveryExpectMsg == null) {
			deliveryExpectMsg = "-- ��� ������ �˸� --\r\n";
		}
		deliveryExpectMsg = deliveryExpectMsg.concat(msg + "\r\n");
	}
	
	public void addCompleteMsg(String msg) {
		if(deliveryCompleteMsg == null) {
			deliveryCompleteMsg = "-- ��� �Ϸ� �˸� --\r\n";
		}
		deliveryCompleteMsg = deliveryCompleteMsg.concat(msg + "\r\n");
	}
	
	public void addTodayMsg(String msg) {
		if(deliveryTodayMsg == null) {
			deliveryTodayMsg = "-- ���� ��� �˸� --\r\n";
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
