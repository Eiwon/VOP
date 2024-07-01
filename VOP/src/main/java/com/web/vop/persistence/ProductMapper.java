package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.web.vop.domain.OrderViewDTO;
import com.web.vop.domain.ProductDetailsDTO;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ProductVO;
import com.web.vop.util.Pagination;

@Mapper
public interface ProductMapper {
	
	// 상품 상세 정보 검색
	ProductVO selectProduct(int productId);
	
	// 상품 미리보기 검색
	ProductPreviewDTO selectPreviewById(int productId);
	
	// 댓글 총 갯수 검색
	int selectReviewCount(int productId);
	
	// 댓글 총 갯수 증가
	int reviewNumUP(int productId);
			
	// 댓글 총 갯수 감소
	int reviewNumDown(int productId);
		
	// 상품 리뷰(별) 총합 검색 // 사용안함
//	int selectReviewStar(int productId);
	
	// 상품 리뷰(별) 평균값 등록
	int updateReviewAvg(@Param("productId")int productId, @Param("reviewAvg")String reviewAvg);
	
	// 상품 리뷰(별) 평균값 수정
	int updateReviewAvgNew(@Param("productId")int productId, @Param("reviewAvg")float reviewAvg);
	
	// 댓글(리뷰) 카운터
	int updateReviewNum(@Param("productId")int productId, @Param("reviewNum")int reviewNum);
	
	// 상품 등록
	int insertProduct(ProductVO productVO);
	
	// 썸네일이 있는 상품 등록
	int insertProductWithThumbnail(ProductVO productVO);
	
	// 검색어 또는 카테고리 검색 (페이징)
	List<ProductPreviewDTO> selectByNameNCategory(Pagination pagination);
	
	// 검색어 또는 카테고리 검색 결과 수
	int selectByNameNCategoryCnt(Pagination pagination);
	
	// memberId로 상품 조회
	List<ProductPreviewDTO> selectByMemberId(@Param("memberId") String memberId, @Param("pagination") Pagination pagination);
	
	// memberId로 상품 갯수 조회
	int selectByMemberIdCnt(String memberId);
	
	// productId로 상품 상태 변경
	int updateState(@Param("productState") String productState, @Param("productId") int productId);
	
	// productId로 상품 상태 검색
	String selectStateByProductId(int productId);
	
	// productId로 상품 삭제
	int deleteProduct(int productId);
	
	// 카테고리별 리뷰 수가 가장 많은 5개의 상품 검색
	List<ProductPreviewDTO> selectTopProductByCategory();
	
	// 최근 등록된 상품 5개 조회
	List<ProductPreviewDTO> selectRecent5();
	
	// 상태가 ??인 상품 조회
	List<ProductPreviewDTO> selectStateIs(@Param("productState") String productState, @Param("pagination") Pagination pagination);
	
	// 상태가 ??인 상품 수 조회
	int selectStateIsCnt(String productState);
	
	// productId로 상세 정보 조회
	ProductDetailsDTO selectDetails(int productId);
	
	// 상품 정보 변경
	int updateProduct(ProductVO productVO);
	
	// productId로 판매자 memberId 검색
	String selectMemberIdById(int productId);
	
	// 상품 재고 증감
	int updateRemains(@Param("productId") int productId, @Param("increaseNum") int increaseNum);
	
	// 상품 재고 검색
	int selectRemainsById(int productId);
	
	// 상품 상세 정보를 검색하여 OrderViewDTO로 반환 
	List<OrderViewDTO> selectToOrderById(int[] productIds);
	
	
}
