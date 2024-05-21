package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.DeliveryVO;

@Mapper
public interface DeliveryMapper {
	
	// ����� ���
	int insertDelivery(DeliveryVO deliveryVo);
	
	// ����� ���� 
	public int updateDelivery(DeliveryVO deliveryVo);
		
	// ����� ����
	public int deleteDelivery(int deliveryId);
	
	// ����� �� ��ȸ (�ֹ���ȸ)
	public List<DeliveryVO> selectByMemberId(String memberId);
	
	// ����� �� ��ȸ (����������) by deliveryId and memberId
	public DeliveryVO selectBydeliveryId(@Param("deliveryId") int deliveryId, @Param("memberId") String memberId);

<<<<<<< HEAD
	// �⺻ ����� ���� ��ȸ
	int cntIsDefault(@Param("memberId") String memberId);
	
	// ���� �⺻ ������� 0���� ������Ʈ
	int updateDefault(String memberId);
	
	// ���ο� �⺻ ������� 1�� ����
 	int updateNewDefault(@Param("deliveryId") int deliveryId, @Param("memberId") String memberId);
=======
	// memberId�� �⺻ ����� �˻�
	public DeliveryVO selectDefaultByMemberId(String memberId);
>>>>>>> 482ef065dc7c83123d2e8bd29eccb170ee022ed9
	
}
