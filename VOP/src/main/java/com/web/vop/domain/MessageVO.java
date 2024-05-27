package com.web.vop.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {
	
	private int messageId;
	private String type;
	private String title;
	private String content;
	private String writerId;
	private String receiverId;
	private Date dateCreated;
	private String callbackInfo;

}
