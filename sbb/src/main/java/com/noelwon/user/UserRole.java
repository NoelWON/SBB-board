package com.noelwon.user;

import lombok.Getter;

//  사용자에게 부여할 권한 등 관련 내용을 부여할 수 있도록 하기 위해서
@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
