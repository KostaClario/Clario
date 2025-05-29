package com.oopsw.clario.controller;

import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.config.auth.authdto.SessionUser;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.dto.MemberUpdateDTO;
import com.oopsw.clario.exception.EmailAlreadyExistsException;
import com.oopsw.clario.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 모달 테스트용(추후 메인페이지)
    @GetMapping("/modal")
    public String modal() {
        return "account/modal";
    }

    @PostMapping("/account/reset-password")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@AuthenticationPrincipal OAuth2User user,
                                           @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        String email = user.getAttribute("email");

        if(!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        memberService.resetPassword(email, newPassword);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/account/verify-password")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@AuthenticationPrincipal OAuth2User user,
                                            @RequestBody Map<String, String> request) {

        String password = request.get("password");
        String email = user.getAttribute("email");

        boolean result = memberService.checkPassword(email, password);

        if(result) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
        }
    }


    @GetMapping("/account/remove")
    public String remove() {
        return "account/user-remove";
    }

    @PostMapping("/account/remove")
    public String remove(@AuthenticationPrincipal OAuth2User user,
                         @RequestParam String password,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         Authentication authentication,
                         Model model) {

        String email = user.getAttribute("email");

        if(!memberService.checkPassword(email, password)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "account/user-remove";
        }

        memberService.removeMember(email);

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/loginView";
    }

    @GetMapping("/account/edit")
    public String updateInfoView(Model model,@AuthenticationPrincipal OAuth2User user) {
        String email = user.getAttribute("email");

        Member member = memberService.getMemberByEmail(email);

        MemberUpdateDTO dto = new MemberUpdateDTO();
        dto.setName(member.getName());
        dto.setPhonenum(member.getPhonenum());

        model.addAttribute("updateForm", dto);

        return "account/user-info-edit";
    }

    @PostMapping("/account/edit")
    public String updateInfo(@ModelAttribute("updateForm") MemberUpdateDTO dto,
                             @AuthenticationPrincipal OAuth2User user,
                             Model model) {
        String email = user.getAttribute("email");
        if(!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "account/user-info-edit";
        }

        memberService.resetMemberInfo(email,dto);
        return "redirect:/modal";
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
            session.setAttribute("redirectAfterLogin", "/modal");

            return "redirect:/modal";

        }catch (EmailAlreadyExistsException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("email", email);
            return "account/join";
        }
    }
}
