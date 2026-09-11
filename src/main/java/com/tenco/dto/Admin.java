package com.tenco.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password") // ToString을 출력할 때 password 부분을 제외
@Builder
public class Admin {
    private int id;
    private String adminId;
    private String password;
    private String name;

    public Admin(String adminId, String password, String name) {
        this.adminId = adminId;
        this.password = password;
        this.name = name;
    }
}
