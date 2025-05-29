package com.oopsw.clario.controller;


import com.oopsw.clario.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("send-code")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok().body("인증 코드가 전송되었습니다.");
    }

    @PostMapping("verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        boolean verified = emailService.checkVerificationCode(email, code);
        return verified
                ? ResponseEntity.ok(Collections.singletonMap("verified", true))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body(Collections.singletonMap("verified", false));
    }
}
