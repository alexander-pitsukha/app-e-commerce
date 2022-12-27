package com.flatlogic.app.ecomerce.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdatePasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

}
