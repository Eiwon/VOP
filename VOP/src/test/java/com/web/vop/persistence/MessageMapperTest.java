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
import com.web.vop.domain.MessageVO;
import com.web.vop.util.Pagination;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, RootConfig.class, ServletConfig.class})
@Log4j
public class MessageMapperTest {
	
	@Autowired
	private MessageMapper messageMapper;
	 
	@Test
	public void test() {
		MessageVO messageVO = new MessageVO();
        String receiverId = "test1234";
        int messageId = 1000;
        Pagination pagination = new Pagination();
        String memberId = "test1234";
        messageVO.setType("notice");
        messageVO.setTitle("title");
        messageVO.setContent("content");
        messageVO.setReceiverId(receiverId);
        messageVO.setWriterId(memberId);
        
        insertMessageTest(messageVO);
        selectByReceiverIdTest(receiverId);
        deleteByIdTest(messageId);
        deleteByReceiverIdTest(receiverId);
        selectNoticeTest();
        selectByIdTest(messageId);
        selectAllPopupPagingTest(pagination);
        selectAllPopupCntTest();
        selectMyPopupIdTest(memberId);
	}
	
	// �޼��� ���
    public void insertMessageTest(MessageVO messageVO) {
        log.info(messageMapper.insertMessage(messageVO));
    } // end insertMessageTest

    // receiver id�� ���� ������ ��� �޼��� �˻�
    public void selectByReceiverIdTest(String receiverId) {
        log.info(messageMapper.selectByReceiverId(receiverId));
    } // end selectByReceiverIdTest

    // ���� �޼��� ����
    public void deleteByIdTest(int messageId) {
        log.info(messageMapper.deleteById(messageId));
    } // end deleteByIdTest

    // ���� ������ ���� �޽��� ����
    public void deleteByReceiverIdTest(String receiverId) {
        log.info(messageMapper.deleteByReceiverId(receiverId));
    } // end deleteByReceiverIdTest

    // ��� ���� �˻�
    public void selectNoticeTest() {
        log.info(messageMapper.selectNotice());
    } // end selectNoticeTest

    // messageId�� �˻�
    public void selectByIdTest(int messageId) {
        log.info(messageMapper.selectById(messageId));
    } // end selectByIdTest

    // ��� �˾� ���� �˻�
    public void selectAllPopupPagingTest(Pagination pagination) {
        log.info(messageMapper.selectAllPopupPaging(pagination));
    } // end selectAllPopupPagingTest

    // ��� �˾� ���� �˻� ��
    public void selectAllPopupCntTest() {
        log.info(messageMapper.selectAllPopupCnt());
    } // end selectAllPopupCntTest

    // ��������, �������� ���� ���� ���� id �˻�
    public void selectMyPopupIdTest(String memberId) {
        log.info(messageMapper.selectMyPopupId(memberId));
    } // end selectMyPopupIdTest
	
	
}
