package com.oopsw.clario.controller;

import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.config.auth.authdto.SessionUser;
import com.oopsw.clario.exception.EmailAlreadyExistsException;
import com.oopsw.clario.service.MemberService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session) {
        String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
        if (redirectAfterLogin != null) {
            session.removeAttribute("redirectAfterLogin");
            return "redirect:" + redirectAfterLogin;
        }
        return "redirect:/loginView";
    }

    @GetMapping("/template-test")
    public String testTemplate() {            //// thymeleaf 테스트
        System.out.println("🔥 /template-test 호출됨");
        return "account/login";
    }

    @GetMapping("/loginView")
    public String loginView() {
        return "account/login";
    }

    @GetMapping("/privacy")
    public String privacy(@AuthenticationPrincipal OAuth2User user, HttpSession session) {
        if (user == null) return "redirect:/loginView";

        SessionUser sessionUser = (SessionUser) session.getAttribute("user");

        if(memberService.existsByEmail(sessionUser.getEmail())) {
            return "redirect:/account/modal";
        }
        return "account/privacy";
    }

    @GetMapping("/agree")
    public String agree() {
        return "account/join";
    }

    @PostMapping("/join")
    public String join(@RequestParam String name,
                       @RequestParam String phonenum,
                       @RequestParam String password,
                       @RequestParam String confirmPassword,
                       HttpSession session,
                       Model model) {
        OAuthAttributes attributes = (OAuthAttributes) session.getAttribute("oauthAttributes");
        if(attributes == null) {
            return "redirect:/loginView";
        }

        String email = attributes.getEmail();

        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("email", email);
            return "account/join";
        }
        try{
            if(!memberService.existsByEmail(email)){
                memberService.saveMember(email,name,phonenum,password);
            }

            session.removeAttribute("oauthAttributes");
            session.setAttribute("redirectAfterLogin", "/account/modal");

            return "redirect:/account/modal";

        }catch (EmailAlreadyExistsException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("email", email);
            return "account/join";
        }
    }
}
