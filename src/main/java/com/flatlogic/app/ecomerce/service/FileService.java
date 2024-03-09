package com.flatlogic.app.ecomerce.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String UPLOAD_LOCATION = "upload";
    String FOLDER_SEPARATE = "/";
    String PRODUCT_LOCATION = UPLOAD_LOCATION + FOLDER_SEPARATE + "products";
    String USER_LOCATION = UPLOAD_LOCATION + FOLDER_SEPARATE + "users";
    String IMAGE_LOCATION = PRODUCT_LOCATION + FOLDER_SEPARATE + "image";
    String AVATAR_LOCATION = USER_LOCATION + FOLDER_SEPARATE + "avatar";

    Resource downloadFile(String privateUrl);

    void uploadProductsFile(MultipartFile file, String filename);

    void uploadUsersFile(MultipartFile file, String filename);

}
