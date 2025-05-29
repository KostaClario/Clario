package com.oopsw.clario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {

    private String email;
    private String name;
    private String phonenum;
    private String newPassword;
    private String confirmPassword;
}
