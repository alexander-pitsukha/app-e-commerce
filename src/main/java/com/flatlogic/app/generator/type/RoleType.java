package com.flatlogic.app.generator.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum RoleType implements GrantedAuthority {

    USER("user"), ADMIN("admin");

    private final String authority;

    public static RoleType valueOfRole(String role) {
        for (RoleType entry : values()) {
            if (entry.authority.equals(role)) {
                return entry;
            }
        }
        return null;
    }

}
