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
    
    // 마이페이지에서 주문목록 > 배송조회 페이지로 이동
    @GetMapping("/delivery")
    public void deliveryGET() {
        log.info("delivery 페이지 이동 요청");
    }//end deliveryGET()
    
    // 배송지 관리 페이지로 이동
    @GetMapping("/deliveryAddressList")
    public String deliveryAddressListGET(Model model, HttpServletRequest request) {
        log.info("deliveryAddressListGET() 페이지 이동 요청");
        
        String path = "";
        String memberId = (String) request.getSession().getAttribute("memberId");
        log.info("memberId : " + memberId);
        
        if (memberId == null) {
            path = "redirect:../member/login";
        } else {
            // 배송지 상세 조회
            List<DeliveryVO> deliveryList = deliveryService.getMemberId(memberId);
            log.info(deliveryList.toString());
            
            model.addAttribute("deliveryList", deliveryList);
            
            String memberAuth = memberService.getMemberAuth(memberId);
            model.addAttribute("memberAuth", memberAuth);
            
            path = "/Delivery/deliveryAddressList";
        }
        return path;
    }//end deliveryAddressListGET()
    
    // 배송지 등록 페이지로 이동
    @GetMapping("/deliveryRegister")
    public void deliveryRegisterGET() {
        log.info("deliveryRegister 페이지 이동 요청");
    }//end deliveryRegisterGET()
    
    @PostMapping("/register")
    public String registerPOST(DeliveryVO deliveryVo, HttpServletRequest request) {
        log.info("배송 정보 등록");
        
        deliveryVo.setMemberId((String) request.getSession().getAttribute("memberId"));
        deliveryVo.setReceiverAddress(request.getParameter("receiverAddress"));
        
        int res = deliveryService.registerDelivery(deliveryVo);
        log.info(res + "행 등록 성공");
        
        return "redirect:deliveryAddressList";
    }

    // 배송지 수정 페이지로 이동
    @GetMapping("/deliveryUpdate")
    public String deliveryUpdateGET(@RequestParam("deliveryId") int deliveryId, Model model, HttpServletRequest request) {
        log.info("deliveryUpdate 페이지 이동 요청");
        
        String path = "";
        String memberId = (String) request.getSession().getAttribute("memberId");
        log.info("memberId : " + memberId);
        
        if (memberId == null) {
            path = "redirect:../member/login";
        } else {
        	
        // 배송지 전체 조회(by deliveryId)하기
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId);
        log.info("delivery" + delivery);
        
        // 사용자가 선택한 배송지 선택 정보 View로 전달
        model.addAttribute("delivery", delivery);
        
        // 사용자가 선택한 배송지의 deliveryId View로 전달
        model.addAttribute("deliveryId", deliveryId);
        
        String memberAuth = memberService.getMemberAuth(memberId);
        model.addAttribute("memberAuth", memberAuth);
        
        path = "/Delivery/deliveryUpdate";
    	}
	    return path;
    }//end deliveryUpdateGET()
    
    
    @PostMapping("/update")
    public String updatePOST(DeliveryVO deliveryVo,HttpServletRequest request, RedirectAttributes reAttr) {
        log.info("배송 정보 수정");
        
        // 세션에서 memberId 값 가져오기
        String memberId = (String) request.getSession().getAttribute("memberId");
        
        // deliveryVo 객체에 memberId 설정
        deliveryVo.setMemberId(memberId);
        
        int res = deliveryService.updateDelivery(deliveryVo);
        log.info(res + "행 수정 성공");
        
        return "redirect:deliveryAddressList";
    }//end updatePOST()
    
    @GetMapping("/popupSelect")
    public void popupSelectGET() {
    	log.info("배송지 선택 팝업 요청 ");
    } // end popupSelectGET
    
    @GetMapping("/popupRegister")
	public void popupRegisterGET() {
		log.info("배송지 등록 팝업 이동");
	} // end popupRegisterGET
    
}//end DeliveryController
