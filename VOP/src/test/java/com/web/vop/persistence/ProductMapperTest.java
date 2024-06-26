package com.web.vop.persistence;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.web.vop.config.PaymentAPIConfig;
import com.web.vop.config.RootConfig;
import com.web.vop.config.S3Config;
import com.web.vop.config.SecurityConfig;
import com.web.vop.config.ServletConfig;
import com.web.vop.config.WebConfig;
import com.web.vop.domain.ProductPreviewDTO;
import com.web.vop.domain.ProductVO;

import jdk.net.SocketFlow.Status;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, SecurityConfig.class, S3Config.class, PaymentAPIConfig.class, ServletConfig.class})
@Log4j
public class ProductMapperTest {
	
	@Rule
	public JUnitRestDocumentation restDocs = new JUnitRestDocumentation();
    
	private MockMvc mockMvc;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	 @Before
	 public void setUp() {
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
	                                   .apply(MockMvcRestDocumentation.documentationConfiguration(restDocs))
	                                   .build();
	 }
	
	@Test
	public void Test() {
		try {
			selectProduct();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectProduct() throws Exception{
		mockMvc.perform(get("/product/bestReview")).andExpect(status().isOk()).andDo(document("product_test"));
		ProductVO result = productMapper.selectProduct(1);
		log.info(result);
	}
	
}
