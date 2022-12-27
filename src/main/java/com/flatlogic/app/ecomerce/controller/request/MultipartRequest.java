package com.flatlogic.app.ecomerce.controller.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MultipartRequest {

    private MultipartFile file;

    private String filename;

}
