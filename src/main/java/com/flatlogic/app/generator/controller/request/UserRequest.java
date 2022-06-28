package com.flatlogic.app.generator.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String role;

    private boolean disabled;

    private String password;

    private boolean emailVerified;

    private String emailVerificationToken;

    private LocalDateTime emailVerificationTokenExpiresAt;

    private String passwordResetToken;

    private LocalDateTime passwordResetTokenExpiresAt;

    private String provider;

    private String importHash;

    @JsonProperty("wishlist")
    private List<UUID> productIds;

    @JsonProperty("avatar")
    private List<FileRequest> fileRequests;

}
