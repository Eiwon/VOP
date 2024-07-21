package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.web.vop.config.RootConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.ImageVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class ImageMapperTest {

	@Autowired
	public ImageMapper imageMapper;
	
	@Test
	public void test() {
	    ImageVO imageVO = new ImageVO();
	    imageVO.setImgId(1000);
	    imageVO.setProductId(1);
	    imageVO.setImgExtension("jpg");
	    imageVO.setImgPath("productThumbnail/");
	    imageVO.setImgRealName("test");
	    imageVO.setImgChangeName("5881340c-9b07-4471-94a8-5d38e3bee64b");

	    int imgId = imageVO.getImgId();
	    int productId = imageVO.getProductId();

	    selectByImgIdTest(imgId);
//	    insertImgTest(imageVO);
//	    insertProductDetailsImgTest(imageVO);
	    selectByProductIdTest(productId);
//	    selectImgPathByImgIdTest(imgId);
//	    selectImgIdByProductIdTest(productId);
//	    selectAllByProductIdTest(productId);
//	    deleteProductImageTest(productId);
//	    deleteByIdTest(imgId);
//	    deleteByProductIdTest(productId);
//	    updateByIdTest(imageVO);
	} // end test

	// 상품 메인 이미지 검색 
	public void selectByImgIdTest(int imgId) {
	    log.info(imageMapper.selectByImgId(imgId));
	} // end selectByImgIdTest

	// 이미지 등록
	public void insertImgTest(ImageVO imageVO) {
	    log.info(imageMapper.insertImg(imageVO));
	} // end insertImgTest

	// 상품 상세정보 이미지 등록
	public void insertProductDetailsImgTest(ImageVO imageVO) {
	    log.info(imageMapper.insertProductDetailsImg(imageVO));
	} // end insertProductDetailsImgTest

	// productId로 이미지 검색
	public void selectByProductIdTest(int productId) {
	    log.info(imageMapper.selectByProductId(productId));
	} // end selectByProductIdTest

	// 이미지 경로 조회
	public void selectImgPathByImgIdTest(int imgId) {
	    log.info(imageMapper.selectImgPathByImgId(imgId));
	} // end selectImgPathByImgIdTest

	// productId로 이미지 id 검색
	public void selectImgIdByProductIdTest(int productId) {
	    log.info(imageMapper.selectImgIdByProductId(productId));
	} // end selectImgIdByProductIdTest

	// 상품과 관련된 모든 이미지 검색
	public void selectAllByProductIdTest(int productId) {
	    log.info(imageMapper.selectAllbyProductId(productId));
	} // end selectAllByProductIdTest

	// 상품과 관련된 모든 이미지 삭제
	public void deleteProductImageTest(int productId) {
	    log.info(imageMapper.deleteProductImage(productId));
	} // end deleteProductImageTest

	// imgId로 이미지 삭제
	public void deleteByIdTest(int imgId) {
	    log.info(imageMapper.deleteById(imgId));
	} // end deleteByIdTest

	// productId로 이미지 삭제
	public void deleteByProductIdTest(int productId) {
	    log.info(imageMapper.deleteByProductId(productId));
	} // end deleteByProductIdTest

	// imgId로 업데이트
	public void updateByIdTest(ImageVO imageVO) {
	    log.info(imageMapper.updateById(imageVO));
	} // end updateByIdTest
	
}
