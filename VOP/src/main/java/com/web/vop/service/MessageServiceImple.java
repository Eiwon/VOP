package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.MessageVO;
import com.web.vop.persistence.MessageMapper;
import com.web.vop.persistence.ProductMapper;
import com.web.vop.util.PageMaker;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MessageServiceImple implements MessageService{

	@Autowired
	MessageMapper messageMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	@Override
	public int registerMessage(MessageVO messageVO) {
		log.info("registerMessage");
		int res = messageMapper.insertMessage(messageVO);
		return res;
	} // end registerMessage

	@Override
	public List<MessageVO> getMyMessage(String receiverId) {
		log.info("getMyMessage");
		return messageMapper.selectByReceiverId(receiverId);
	} // end getMyMessage

	@Override
	public int removeById(List<Integer> messageIds) {
		log.info("removeById");
		int res = 0;
		for(int messageId : messageIds) {
			res += messageMapper.deleteById(messageId);
		}
		return res;
	} // end removeById

	@Override
	public int removeByReceiverId(String receiverId) {
		log.info("removeByReceiverId");
		return messageMapper.deleteByReceiverId(receiverId);
	} // end removeByReceiverId

	@Override
	public MessageVO getById(int messageId) {
		log.info("getById");
		return messageMapper.selectById(messageId);
	} // end getById

	@Override
	public String getSellerIdOf(int productId) {
		log.info("상품 판매자 id 검색");
		return productMapper.selectMemberIdById(productId);
	} // end getSellerIdOf

	@Override
	public List<MessageVO> getAllPopup(PageMaker pageMaker) {
		log.info("모든 팝업광고 검색 (페이징)");
		pageMaker.setTotalCount(messageMapper.selectAllPopupCnt());
		List<MessageVO> list = messageMapper.selectAllPopupPaging(pageMaker.getPagination());
		return list;
	} // end getAllPopup

	@Override
	public List<Integer> getMyPopupId(String memberId) {
		log.info("모든 공지사항, 수령하지 않은 쿠폰 검색");
		List<Integer> list = (memberId == null) ? 
				messageMapper.selectNotice() : messageMapper.selectMyPopupId(memberId);
		return list;
	} // end getAllPopup
}
