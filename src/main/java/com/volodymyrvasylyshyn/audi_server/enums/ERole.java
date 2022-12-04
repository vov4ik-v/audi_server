package com.volodymyrvasylyshyn.audi_server.enums;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {

    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
