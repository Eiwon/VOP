package com.web.vop.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.vop.domain.DeliveryVO;
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
    public String deliveryAddressListGET(Model model, HttpServletRequest request) {
        log.info("deliveryAddressListGET() ������ �̵� ��û");
        
        String path = "";
        String memberId = (String) request.getSession().getAttribute("memberId");
        log.info("memberId : " + memberId);
        
        if (memberId == null) {
            path = "redirect:../member/login";
        } else {
            // ����� �� ��ȸ
            List<DeliveryVO> deliveryList = deliveryService.getMemberId(memberId);
            log.info(deliveryList.toString());
            
            model.addAttribute("deliveryList", deliveryList);
            
            String memberAuth = memberService.getMemberAuth(memberId);
            model.addAttribute("memberAuth", memberAuth);
            
            path = "/Delivery/deliveryAddressList";
        }
        return path;
    }//end deliveryAddressListGET()
    
    // ����� ��� �������� �̵�
    @GetMapping("/deliveryRegister")
    public void deliveryRegisterGET() {
        log.info("deliveryRegister ������ �̵� ��û");
    }//end deliveryRegisterGET()
    
    @PostMapping("/register")
    public String registerPOST(DeliveryVO deliveryVo, HttpServletRequest request) {
        log.info("��� ���� ���");
        
        deliveryVo.setMemberId((String) request.getSession().getAttribute("memberId"));
        deliveryVo.setReceiverAddress(request.getParameter("receiverAddress"));
        
        int res = deliveryService.registerDelivery(deliveryVo);
        log.info(res + "�� ��� ����");
        
        return "redirect:deliveryAddressList";
    }

    // ����� ���� �������� �̵�
    @GetMapping("/deliveryUpdate")
    public String deliveryUpdateGET(@RequestParam("deliveryId") int deliveryId, Model model, HttpServletRequest request) {
        log.info("deliveryUpdate ������ �̵� ��û");
        
        String path = "";
        String memberId = (String) request.getSession().getAttribute("memberId");
        log.info("memberId : " + memberId);
        
        if (memberId == null) {
            path = "redirect:../member/login";
        } else {
        	
        // ����� ��ü ��ȸ(by deliveryId)�ϱ�
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId);
        log.info("delivery" + delivery);
        
        // ����ڰ� ������ ����� ���� ���� View�� ����
        model.addAttribute("delivery", delivery);
        
        // ����ڰ� ������ ������� deliveryId View�� ����
        model.addAttribute("deliveryId", deliveryId);
        
        String memberAuth = memberService.getMemberAuth(memberId);
        model.addAttribute("memberAuth", memberAuth);
        
        path = "/Delivery/deliveryUpdate";
    	}
	    return path;
    }//end deliveryUpdateGET()
    
    
    @PostMapping("/update")
    public String updatePOST(DeliveryVO deliveryVo,HttpServletRequest request, RedirectAttributes reAttr) {
        log.info("��� ���� ����");
        
        // ���ǿ��� memberId �� ��������
        String memberId = (String) request.getSession().getAttribute("memberId");
        
        // deliveryVo ��ü�� memberId ����
        deliveryVo.setMemberId(memberId);
        
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
