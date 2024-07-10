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
		String productState = "판매중";
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
	// 상품 상세 정보 검색
	// input : productId, output : productVO
	public void selectProduct(int productId) {
		//mockMvc.perform(get("/product/bestReview")).andExpect(status().isOk()).andDo(document("product_test"));
		ProductVO result = productMapper.selectProduct(productId);
		log.info(result);
	} // end selectProduct
	
	// 상품 미리보기 검색
	// input : productId, output : productPreviewDTO
	public void selectPreviewById(int productId) {
		ProductPreviewDTO result = productMapper.selectPreviewById(productId);
		log.info(result);
	} // end selectPreviewById
	
	// 댓글 총 갯수 검색
	// input : productId, output : reviewCount
	public void selectReviewCount(int productId) {
		int reviewCount = productMapper.selectReviewCount(productId);
		log.info(reviewCount);
	} // end selectReviewCount
	
	// 댓글 총 갯수 증가
	// input : productId, output : int
	public void reviewNumUP(int productId) {
		int res = productMapper.reviewNumUP(productId);
		log.info(res);
	} // end reviewNumUP
	
	// 댓글 총 갯수 감소
	// input : productId, output : int
	public void reviewNumDown(int productId) {
		int res = productMapper.reviewNumDown(productId);
		log.info(res);
	} // end reviewNumDown
	
	// 상품 리뷰(별) 평균값 등록(
	public void updateReviewAvg(int productId, float reviewAvg) {
		int res = productMapper.updateReviewAvg(productId, reviewAvg);
		log.info(res);
	} // end updateReviewAvg
		
//	// 상품 리뷰(별) 평균값 수정(사용 안함)
//	public void updateReviewAvgNew(int productId, float reviewAvg) {
//		int res = productMapper.updateReviewAvgNew(productId, reviewAvg);
//		log.info(res);
//	} // end updateReviewAvgNew
	
	// 댓글(리뷰) 카운터
	public void updateReviewNum(int productId, int reviewNum) {
		int res = productMapper.updateReviewNum(productId, reviewNum);
		log.info(res);
	} // end updateReviewNum
		
	// 상품 등록
	public void insertProduct(ProductVO productVO) {
		int res = productMapper.insertProduct(productVO);
		log.info(res);
	} // end insertProduct
			
	// 썸네일이 있는 상품 등록
	public void insertProductWithThumbnail(ProductVO productVO) {
		int res = productMapper.insertProductWithThumbnail(productVO);
		log.info(res);
	} // end insertProductWithThumbnail
		
	// 검색어 또는 카테고리 검색 (페이징)
	public void selectByNameNCategory(Pagination pagination) {
		log.info(productMapper.selectByNameNCategory(pagination));
	} // end selectByNameNCategory
		
	// 검색어 또는 카테고리 검색 결과 수
	public void selectByNameNCategoryCnt(Pagination pagination) {
		log.info(productMapper.selectByNameNCategoryCnt(pagination));
	} // end selectByNameNCategoryCnt
		
	// memberId로 상품 조회
	public void selectByMemberId(String memberId, Pagination pagination) {
		log.info(productMapper.selectByMemberId(memberId, pagination));
	} // end selectByMemberId
		
	// memberId로 상품 갯수 조회
	public void selectByMemberIdCnt(String memberId) {
		log.info(productMapper.selectByMemberIdCnt(memberId));
	} // end selectByMemberIdCnt
		
	// productId로 상품 상태 변경
	public void updateState(String productState, int productId) {
		log.info(productMapper.updateState(productState, productId));
	} // end updateState
		
	// productId로 상품 상태 검색
	public void selectStateByProductId(int productId) {
		log.info(productMapper.selectStateByProductId(productId));
	} // end selectStateByProductId
		
	// productId로 상품 삭제
	public void deleteProduct(int productId) {
		log.info(productMapper.deleteProduct(productId));
	} // end deleteProduct
		
	// 카테고리별 리뷰 수가 가장 많은 5개의 상품 검색
	public void selectTopProductByCategory(){
		log.info(productMapper.selectTopProductByCategory());
	} // end selectTopProductByCategory
		
	// 최근 등록된 상품 5개 조회
	public void selectRecent5(){
		log.info(productMapper.selectRecent5());
	} // end selectRecent5
		
	// 상태가 ??인 상품 조회
	public void selectStateIs(String productState, Pagination pagination){
		log.info(productMapper.selectStateIs(productState, pagination));
	} // end selectStateIs
		
	// 상태가 ??인 상품 수 조회
	public void selectStateIsCnt(String productState){
		log.info(productMapper.selectStateIsCnt(productState));
	} // end selectStateIsCnt
		
	// productId로 상세 정보 조회
	public void selectDetails(int productId){
		log.info(productMapper.selectDetails(productId));
	} // end selectDetails
		
	// 상품 정보 변경
	public void updateProduct(ProductVO productVO){
		log.info(productMapper.updateProduct(productVO));
	} // end updateProduct
		
	// productId로 판매자 memberId 검색
	public void selectMemberIdById(int productId){
		log.info(productMapper.selectMemberIdById(productId));
	} // end selectMemberIdById
		
	// 상품 재고 증감
	public void updateRemains(int productId, int increaseNum){
		log.info(productMapper.updateRemains(productId, increaseNum));
	} // end updateRemains
		
	// 상품 재고 검색
	public void selectRemainsById(int productId){
		log.info(productMapper.selectRemainsById(productId));
	} // end selectRemainsById
		
	// 상품 상세 정보를 검색하여 OrderViewDTO로 반환 
	public void selectToOrderById(int[] productIds){
		log.info(productMapper.selectToOrderById(productIds));
	} // end selectToOrderById
}
