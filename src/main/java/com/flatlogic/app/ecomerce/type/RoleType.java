package com.flatlogic.app.ecomerce.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum RoleType implements GrantedAuthority {

    @JsonProperty("user") USER("user"),
    @JsonProperty("admin") ADMIN("admin");

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
