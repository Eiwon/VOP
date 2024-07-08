package com.web.vop.persistence;


import java.util.List;

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
import com.web.vop.domain.DeliveryExpectDTO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, SecurityConfig.class, S3Config.class, PaymentAPIConfig.class, ServletConfig.class})
@Log4j
public class OrderMapperTest {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Test
	public void Test() {
		try {
			selectDeliveryExpect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectDeliveryExpect() throws Exception{
		List<DeliveryExpectDTO> list = orderMapper.selectDeliveryExpect();
		for(DeliveryExpectDTO dto : list) {
			log.info(dto);
		}
	}
}
