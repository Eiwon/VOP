package com.web.vop.persistence;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.web.vop.config.PaymentAPIConfig;
import com.web.vop.config.RootConfig;
import com.web.vop.config.S3Config;
import com.web.vop.config.SecurityConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.config.WebSocketConfig;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, SecurityConfig.class, S3Config.class, PaymentAPIConfig.class,
		ServletConfig.class, WebSocketConfig.class})
@Log4j
public class ProductMapperTest {
	
	@Autowired
	private ProductMapper productMapper;
	
	@Test
	public void Test() {
		int productId = 1;
		String memberId = "test1234";
		String productState = "�Ǹ���";
		ProductVO productVO = new ProductVO();
		Pagination pagination = new Pagination();
		try {
			//selectProduct(productId);
			//selectPreviewById(productId);
			//selectReviewCount(productId);
			//reviewNumUP(productId);
			//reviewNumDown(productId);
			//updateReviewAvg(productId, "0");
			//updateReviewAvgNew(productId, (float) 1.5);
			//updateReviewNum(productId, 0);
			//insertProduct(productVO);
			//insertProductWithThumbnail(productVO);
			//selectByNameNCategory(pagination);
			//selectByNameNCategoryCnt(pagination);
			//selectByMemberId(memberId, pagination);
			//selectByMemberIdCnt(memberId);
			//updateState(productState, productId);
			//selectStateByProductId(productId);
			//deleteProduct(productId);
			//selectTopProductByCategory();
			//selectRecent5();
			//selectStateIs(productState, pagination);
			//selectStateIsCnt(productState);
			//selectDetails(productId);
			//updateProduct(productVO);
			//selectMemberIdById(productId);
			updateRemains(productId, 1);
			//selectRemainsById(productId);
			selectToOrderById(new int[5]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ��ǰ �� ���� �˻�
	// input : productId, output : productVO
	public void selectProduct(int productId) {
		//mockMvc.perform(get("/product/bestReview")).andExpect(status().isOk()).andDo(document("product_test"));
		ProductVO result = productMapper.selectProduct(productId);
		log.info(result);
	} // end selectProduct
	
	// ��ǰ �̸����� �˻�
	// input : productId, output : productPreviewDTO
	public void selectPreviewById(int productId) {
		ProductPreviewDTO result = productMapper.selectPreviewById(productId);
		log.info(result);
	} // end selectPreviewById
	
	// ��� �� ���� �˻�
	// input : productId, output : reviewCount
	public void selectReviewCount(int productId) {
		int reviewCount = productMapper.selectReviewCount(productId);
		log.info(reviewCount);
	} // end selectReviewCount
	
	// ��� �� ���� ����
	// input : productId, output : int
	public void reviewNumUP(int productId) {
		int res = productMapper.reviewNumUP(productId);
		log.info(res);
	} // end reviewNumUP
	
	// ��� �� ���� ����
	// input : productId, output : int
	public void reviewNumDown(int productId) {
		int res = productMapper.reviewNumDown(productId);
		log.info(res);
	} // end reviewNumDown
	
	// ��ǰ ����(��) ��հ� ���(
	public void updateReviewAvg(int productId, float reviewAvg) {
		int res = productMapper.updateReviewAvg(productId, reviewAvg);
		log.info(res);
	} // end updateReviewAvg
		
//	// ��ǰ ����(��) ��հ� ����(��� ����)
//	public void updateReviewAvgNew(int productId, float reviewAvg) {
//		int res = productMapper.updateReviewAvgNew(productId, reviewAvg);
//		log.info(res);
//	} // end updateReviewAvgNew
	
	// ���(����) ī����
	public void updateReviewNum(int productId, int reviewNum) {
		int res = productMapper.updateReviewNum(productId, reviewNum);
		log.info(res);
	} // end updateReviewNum
		
	// ��ǰ ���
	public void insertProduct(ProductVO productVO) {
		int res = productMapper.insertProduct(productVO);
		log.info(res);
	} // end insertProduct
			
	// ������� �ִ� ��ǰ ���
	public void insertProductWithThumbnail(ProductVO productVO) {
		int res = productMapper.insertProductWithThumbnail(productVO);
		log.info(res);
	} // end insertProductWithThumbnail
		
	// �˻��� �Ǵ� ī�װ� �˻� (����¡)
	public void selectByNameNCategory(Pagination pagination) {
		log.info(productMapper.selectByNameNCategory(pagination));
	} // end selectByNameNCategory
		
	// �˻��� �Ǵ� ī�װ� �˻� ��� ��
	public void selectByNameNCategoryCnt(Pagination pagination) {
		log.info(productMapper.selectByNameNCategoryCnt(pagination));
	} // end selectByNameNCategoryCnt
		
	// memberId�� ��ǰ ��ȸ
	public void selectByMemberId(String memberId, Pagination pagination) {
		log.info(productMapper.selectByMemberId(memberId, pagination));
	} // end selectByMemberId
		
	// memberId�� ��ǰ ���� ��ȸ
	public void selectByMemberIdCnt(String memberId) {
		log.info(productMapper.selectByMemberIdCnt(memberId));
	} // end selectByMemberIdCnt
		
	// productId�� ��ǰ ���� ����
	public void updateState(String productState, int productId) {
		log.info(productMapper.updateState(productState, productId));
	} // end updateState
		
	// productId�� ��ǰ ���� �˻�
	public void selectStateByProductId(int productId) {
		log.info(productMapper.selectStateByProductId(productId));
	} // end selectStateByProductId
		
	// productId�� ��ǰ ����
	public void deleteProduct(int productId) {
		log.info(productMapper.deleteProduct(productId));
	} // end deleteProduct
		
	// ī�װ��� ���� ���� ���� ���� 5���� ��ǰ �˻�
	public void selectTopProductByCategory(){
		log.info(productMapper.selectTopProductByCategory());
	} // end selectTopProductByCategory
		
	// �ֱ� ��ϵ� ��ǰ 5�� ��ȸ
	public void selectRecent5(){
		log.info(productMapper.selectRecent5());
	} // end selectRecent5
		
	// ���°� ??�� ��ǰ ��ȸ
	public void selectStateIs(String productState, Pagination pagination){
		log.info(productMapper.selectStateIs(productState, pagination));
	} // end selectStateIs
		
	// ���°� ??�� ��ǰ �� ��ȸ
	public void selectStateIsCnt(String productState){
		log.info(productMapper.selectStateIsCnt(productState));
	} // end selectStateIsCnt
		
	// productId�� �� ���� ��ȸ
	public void selectDetails(int productId){
		log.info(productMapper.selectDetails(productId));
	} // end selectDetails
		
	// ��ǰ ���� ����
	public void updateProduct(ProductVO productVO){
		log.info(productMapper.updateProduct(productVO));
	} // end updateProduct
		
	// productId�� �Ǹ��� memberId �˻�
	public void selectMemberIdById(int productId){
		log.info(productMapper.selectMemberIdById(productId));
	} // end selectMemberIdById
		
	// ��ǰ ��� ����
	public void updateRemains(int productId, int increaseNum){
		log.info(productMapper.updateRemains(productId, increaseNum));
	} // end updateRemains
		
	// ��ǰ ��� �˻�
	public void selectRemainsById(int productId){
		log.info(productMapper.selectRemainsById(productId));
	} // end selectRemainsById
		
	// ��ǰ �� ������ �˻��Ͽ� OrderViewDTO�� ��ȯ 
	public void selectToOrderById(int[] productIds){
		log.info(productMapper.selectToOrderById(productIds));
	} // end selectToOrderById
}
