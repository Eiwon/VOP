package com.web.vop.domain;

import java.util.List;

import com.web.vop.util.PageMaker;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PagingListDTO<T> {

	private List<T> list;
	private PageMaker pageMaker;
}
