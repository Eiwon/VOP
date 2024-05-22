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
    
    
    // 마이페이지에서 주문목록 > 배송조회 페이지로 이동
    @GetMapping("/delivery")
    public void deliveryGET() {
        log.info("delivery 페이지 이동 요청");
    }//end deliveryGET()
    
    
    // 배송지 관리 페이지로 이동
    @GetMapping("/deliveryAddressList")
    public String deliveryAddressListGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryAddressListGET() 페이지 이동 요청");
        
            // 배송지 상세 조회
            List<DeliveryVO> deliveryList = deliveryService.getMemberId(memberDetails.getUsername());
            log.info(deliveryList.toString());
            
            model.addAttribute("deliveryList", deliveryList);
            
        return "/Delivery/deliveryAddressList";
    }//end deliveryAddressListGET()
    
    
    // 배송지 등록 페이지로 이동
    @GetMapping("/deliveryRegister")
    public String deliveryRegisterGET(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryRegister 페이지 이동 요청");
        
        	String memberId = memberDetails.getUsername();
        			
            model.addAttribute("memberId", memberId);
           
        return "/Delivery/deliveryRegister";
    }//end deliveryRegisterGET()

    // 배송지 등록 처리
    @PostMapping("/register")
    public String registerPOST(DeliveryVO deliveryVo, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("배송 정보 등록");
        
        deliveryVo.setMemberId(memberDetails.getUsername());
        
        int res = deliveryService.registerDelivery(deliveryVo);
        log.info(res + "행 등록 성공");
        
        return "redirect:deliveryAddressList";
    }

    // 배송지 수정 페이지로 이동
    @GetMapping("/deliveryUpdate")
    public String deliveryUpdateGET(@RequestParam("deliveryId") int deliveryId, 
    		Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("deliveryUpdate 페이지 이동 요청");
        	
        // 배송지 전체 조회(by deliveryId)하기
        DeliveryVO delivery = deliveryService.getDeliveryById(deliveryId, memberDetails.getUsername());
        log.info("delivery" + delivery);
        
        // 사용자가 선택한 배송지 선택 정보 View로 전달
        model.addAttribute("delivery", delivery);
        
        // 사용자가 선택한 배송지의 deliveryId View로 전달
        model.addAttribute("deliveryId", deliveryId);
        
        // 사용자 아이디만 전달 
        model.addAttribute("memberId", memberDetails.getUsername());
        
	    return "/Delivery/deliveryUpdate";
    }//end deliveryUpdateGET()
    
    
    // 배송지 수정 처리
    @PostMapping("/update")
    public String updatePOST(DeliveryVO deliveryVo,
    		@AuthenticationPrincipal MemberDetails memberDetails, RedirectAttributes reAttr) {
        log.info("배송 정보 수정");
        
        // deliveryVo 객체에 memberId 설정
        deliveryVo.setMemberId(memberDetails.getUsername());
        
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
