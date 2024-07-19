package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.CouponVO;
import com.web.vop.util.Pagination;

@Mapper
public interface CouponMapper {
	
	// �ű� ���� ��� 
	public int insertCoupon(CouponVO couponVO);
	
	// ��� ���� ��ȸ (����¡)
	public List<CouponVO> selectAllCoupon(Pagination pagination);
	
	// ��� ���� �� ��ȸ
	public int selectAllCouponCnt();
	  
	// ���� ����
	public int deleteCouponById(int couponId);
	
	// id�� ���� �˻�
	public CouponVO selectById(int couponId);
	
	// ���� �������� ���� or ������� ���� ���� �˻�
	public List<CouponVO> selectNotHadCoupon(String memberId);
	
	// ���� ���� ����
	public int updatePublishingById(@Param("couponId") int couponId, @Param("publishing") int publishing);

	// ���� ���� ��� ���� �˻�
	public List<CouponVO> selectPublishingCoupon();


}
