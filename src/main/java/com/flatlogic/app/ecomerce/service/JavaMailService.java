package com.flatlogic.app.ecomerce.service;

import java.util.UUID;

public interface JavaMailService {

    void sendEmailForCreateUser(String email, UUID token);

    void sendEmailForUpdateUserPasswordResetToken(String email, UUID token);

}
