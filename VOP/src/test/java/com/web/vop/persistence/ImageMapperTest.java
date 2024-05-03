package com.web.vop.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.vop.config.RootConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.ImageVO;
import com.web.vop.domain.ProductVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class})
@Log4j
public class ImageMapperTest {
	
	@Autowired
	private ImageMapper imageMapper;
	
	@Test
	public void test() {
		testImageByImageId(); // 테스트 성공
	}
	
	// 이미지 상세 검색 테스트
	private void testImageByImageId() {
		ImageVO imageVO = imageMapper.selectByImgId(1);
		log.info("testImageByImageId()" + imageVO);
	}
	
}
