package com.web.vop.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.MemberDetails;
import com.web.vop.service.DeliveryService;
import com.web.vop.service.MemberService;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/Delivery")
@Log4j
public class DeliveryController {
    
    @Autowired
    DeliveryService deliveryService;
    
    @Autowired
    MemberService memberService;
    
    
    // �������������� �ֹ���� > �����ȸ �������� �̵�
    @GetMapping("/delivery")
    public void deliveryGET() {
        log.info("delivery ������ �̵� ��û");
    }//end deliveryGET()
    
    
    // ����� ���� �������� �̵�
    @GetMapping("/deliveryAddressList")
    public String deliveryAddressListGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryAddressListGET() ������ �̵� ��û");
        
            // ����� �� ��ȸ
            List<DeliveryVO> deliveryList = deliveryService.getMemberId(memberDetails.getUsername());
            log.info(deliveryList.toString());
            
            model.addAttribute("deliveryList", deliveryList);
            
        return "/Delivery/deliveryAddressList";
    }//end deliveryAddressListGET()
    
    
    // ����� ��� �������� �̵�
    @GetMapping("/deliveryRegister")
    public String deliveryRegisterGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryRegister ������ �̵� ��û");
        
        	String memberId = memberDetails.getUsername();
        			
            model.addAttribute("memberId", memberId);
           
        return "/Delivery/deliveryRegister";
    }//end deliveryRegisterGET()

    // ����� ��� ó��
    @PostMapping("/register")
    public String registerPOST(DeliveryVO deliveryVo, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("��� ���� ���");
        
        deliveryVo.setMemberId(memberDetails.getUsername());
        
        int res = deliveryService.registerDelivery(deliveryVo);
        log.info(res + "�� ��� ����");
        
        return "redirect:deliveryAddressList";
    }

    // ����� ���� �������� �̵�
    @GetMapping("/deliveryUpdate")
    public String deliveryUpdateGET(@RequestParam("deliveryId") int deliveryId, 
    		Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryUpdate ������ �̵� ��û");
        	
        // ����� ��ü ��ȸ(by deliveryId)�ϱ�
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId, memberDetails.getUsername());
        log.info("delivery" + delivery);
        
        // ����ڰ� ������ ����� ���� ���� View�� ����
        model.addAttribute("delivery", delivery);
        
        // ����ڰ� ������ ������� deliveryId View�� ����
        model.addAttribute("deliveryId", deliveryId);
        
        // ����� ���̵� ���� 
        model.addAttribute("memberId", memberDetails.getUsername());
        
	    return "/Delivery/deliveryUpdate";
    }//end deliveryUpdateGET()
    
    
    // ����� ���� ó��
    @PostMapping("/update")
    public String updatePOST(DeliveryVO deliveryVo,
    		@AuthenticationPrincipal MemberDetails memberDetails, RedirectAttributes reAttr) {
        log.info("��� ���� ����");
        
        // deliveryVo ��ü�� memberId ����
        deliveryVo.setMemberId(memberDetails.getUsername());
        
        int res = deliveryService.updateDelivery(deliveryVo);
        log.info(res + "�� ���� ����");
        
        return "redirect:deliveryAddressList";
    }//end updatePOST()
    
    
    @GetMapping("/popupSelect")
    public void popupSelectGET() {
    	log.info("����� ���� �˾� ��û ");
    } // end popupSelectGET
    
    
    @GetMapping("/popupRegister")
	public void popupRegisterGET() {
		log.info("����� ��� �˾� �̵�");
	} // end popupRegisterGET
    
}//end DeliveryController
