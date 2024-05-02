package com.web.vop.persistence;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderMapper {
	public Date selectByExpectDeliveryDate(Date expectDeliveryDate);
}
