package com.flatlogic.app.ecomerce.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class VerifyEmailRequest {

    @NotBlank
    private String token;

}
