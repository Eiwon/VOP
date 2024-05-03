package com.web.vop.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ������ ��ȣ�� ������ ����� �������� ���� ��ȣ�� �� ��ȣ�� �������ִ� Ŭ����
@Getter
@Setter
@ToString
public class Pagination {
	private int pageNum; // ���� ������ ��ȣ
	private int pageSize; // ���� ������ ������
	private String type; // �˻� �׸�
	private String keyword; // �˻� Ű���� 

	public Pagination() {
		this.pageNum = 1; // �⺻ ������ ��ȣ ����
		this.pageSize = 5; // �⺻ ������ ������ ����
	}

	public Pagination(int page, int pageSize) {
		this.pageNum = page;
		this.pageSize = pageSize;
	}

	// ���õ� �������� ���� �� �Ϸù�ȣ(rn) - #{start}
	public int getStart() {
		return (this.pageNum - 1) * this.pageSize + 1;
	}

	// ���õ� �������� ������ �� �Ϸù�ȣ(rn) - #{end}
	public int getEnd() {
		return this.pageNum * this.pageSize;
	}

} // end Pagination

