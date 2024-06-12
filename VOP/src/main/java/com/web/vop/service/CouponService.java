package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponPocketVO;
import com.web.vop.domain.CouponVO;
import com.web.vop.domain.MyCouponVO;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;

public interface CouponService {

	// �ű� ���� ���
	public int registerCoupon(CouponVO couponVO);
		
	// ��� ���� ��ȸ (����¡)
	public List<CouponVO> getAllCoupon(PageMaker pageMaker);
		  
	// ���� ����
	public int deleteCouponById(int couponId);
	
	// id�� ���� ��ȸ
	public CouponVO getCouponById(int couponId);
		
	// memberId�� ���� ��ȸ
	public List<MyCouponVO> getMyCouponPocket(String memberId);
			
	// memberId�� ��� ������ ���� ��ȸ
	public List<MyCouponVO> getMyUsableCouponPocket(String memberId);
	
	// ���� �߰�
	public int addCouponPocket(int couponId, String memberId);
			
	// ���� ���� ���� �� ����
	//public int setCouponNum(CouponPocketVO couponPocketVO);
			
	// ���� ���� ���� ��ȸ
	//public int getCouponNum(int couponId, String memberId);
			
	// �������� ���� ���� ����
	public int deleteCouponPocket(int couponId, String memberId);
	
	// ���� ��� ó��
	public int useUpCoupon(int couponId, String memberId);
	
	// ���� �������� ���� or ������� ���� ���� �˻�
	public List<CouponVO> getNotHadCoupon(String memberId);
	
	// ���� ���� ����
	public int setPublishing(int couponId, int publishing);
	
	// memberId�� ���� ��ȸ
	//public List<CouponVO> getByMemberId(String memberId);
		  
	// ���� ���� ����
	//public int removeCoupon(CouponVO couponVO);
		
	// ���� ���� ���� �� ����
	//public int setCouponNum(CouponVO couponVO);
	
	// ���� ���� ���� ��ȸ
	//public int getCouponNum(CouponVO couponVO); 
	
	// ���� ��� ó��
	//public int useUpCoupon(CouponVO couponVO);
	
}
