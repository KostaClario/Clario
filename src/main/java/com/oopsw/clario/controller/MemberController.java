package com.oopsw.clario.controller;

import com.oopsw.clario.exception.EmailAlreadyExistsException;
import com.oopsw.clario.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/loginView")
    public String loginView() {
        return "account/login";
    }

    @GetMapping("/privacy")
    public String privacy(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) return "redirect:/loginView";
        return "account/privacy";
    }

    @GetMapping("/agree")
    public String agree() {
        return "account/join";
    }

    @PostMapping("/join")
    public String join(@AuthenticationPrincipal OAuth2User user,
                       @RequestParam String name,
                       @RequestParam String phonenum,
                       @RequestParam String password,
                       @RequestParam String confirmPassword,
                       Model model) {
        try{
            String email = user.getAttribute("email");

            if(!password.equals(confirmPassword)){
                model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
                model.addAttribute("email", user.getAttribute("email"));
                return "account/join";
            }

            if(!memberService.existsByEmail(email)){
                memberService.saveMember(name, phonenum, password, email);
            }

            return "redirect:/account/modal";
        }catch (EmailAlreadyExistsException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "account/join";
        }
    }
}
