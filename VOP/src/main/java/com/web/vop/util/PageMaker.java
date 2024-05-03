package com.web.vop.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ������ ��ȣ�� �����ϱ� ���� Ŭ����
@Getter
@Setter
@ToString
public class PageMaker {
	private Pagination pagination; // ����¡ ó�� ��ü
	private int totalCount; // ��ü �Խñ� ��
	private int pageCount; // ȭ�鿡 ǥ�õǴ� ������ ��ȣ ��
	
	public PageMaker() {
		this.pageCount = 5; // 5���� ������ ��ȣ�� ȭ�鿡 ǥ��
	}	
	
	// ��ü ������ ��ȣ ��갪
	private int calcTotalPageNum() {
		// Math.ceil (�ø�)
		// Math.floor (����)		
		return (int) Math.ceil((double) totalCount / pagination.getPageSize()); 
		
	} // end calcTotalPageNum()
	
	// �ӽ� �� ��ȣ ��갪
	private int tempEndNum() {
		return (int) Math.ceil((double) pagination.getPageNum() / pageCount) * pageCount;
	} // end tempEndNum()
	
	// ���� ������ ��ȣ ���
	public int getStartNum() {
		return tempEndNum() - (pageCount - 1);
	} // end getStartNum()
	
	// �� ������ ��ȣ ���
	public int getEndNum() {
		// �ӽ� �� ��ȣ ��갪
		int tempEndNum = tempEndNum();
		int totalPageNum = calcTotalPageNum();
		
		if (tempEndNum > totalPageNum) {
			return totalPageNum; // �� ��ȣ�� ��ü ������ ��ȣ
		} else {
			return tempEndNum; // �� ��ȣ�� �ӽ� �� ��ȣ
		}
	} // end getEndNum()
	
	// ȭ�鿡 ���̴� ���� ������ ��ȣ���� ���� ������ ������ ��ȣ ���� ����
	public boolean isPrev() {
		// prev ���� Ȯ��
		if (getStartNum() <= 1) {
			return false;
		} else {
			return true;
		}
	} // end isPrev()
	
	// ȭ�鿡 ���̴� �� ������ ��ȣ���� ū ������ ������ ��ȣ ���� ����
	public boolean isNext() {
		// next ���� Ȯ��
		if (getEndNum() >= calcTotalPageNum()) {
			return false;
		} else {
			return true;
		}
	} // end isNext()
	
} // end PageMaker