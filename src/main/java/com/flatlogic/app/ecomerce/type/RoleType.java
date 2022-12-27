package com.flatlogic.app.ecomerce.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum RoleType implements GrantedAuthority {

    USER("user"), ADMIN("admin");

    private final String authority;

    public static RoleType valueOfRole(String role) {
        for (RoleType entry : values()) {
            if (Objects.equals(entry.authority, role)) {
                return entry;
            }
        }
        return null;
    }

}
