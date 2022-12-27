package com.flatlogic.app.ecomerce.oauth2;

import com.flatlogic.app.ecomerce.entity.User;
import com.flatlogic.app.ecomerce.repository.UserRepository;
import com.flatlogic.app.ecomerce.type.RoleType;
import com.flatlogic.app.ecomerce.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * GoogleOidcUserServiceImpl service.
 */
@Service(Constants.GOOGLE_OIDC_USER_SERVICE)
@RequiredArgsConstructor
public class GoogleOidcUserServiceImpl extends OidcUserService {

    private static final String GIVEN_NAME = "given_name";
    private static final String FAMILY_NAME = "family_name";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Load OidcUser.
     *
     * @param userRequest OidcUserRequest
     * @return OidcUser
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        var oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();
        var userInfo = new GoogleOAuth2UserInfo();
        userInfo.setFirstName((String) attributes.get(GIVEN_NAME));
        userInfo.setLastName((String) attributes.get(FAMILY_NAME));
        userInfo.setEmail((String) attributes.get(Constants.EMAIL));
        updateUser(userInfo);
        return oidcUser;
    }

    private void updateUser(GoogleOAuth2UserInfo userInfo) {
        var user = userRepository.findByEmail(userInfo.getEmail());
        if (user == null) {
            user = new User();
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setRole(RoleType.USER);
            user.setEmailVerified(Boolean.TRUE);
            user.setDisabled(Boolean.FALSE);
        }
        user.setLastName(userInfo.getLastName());
        user.setFirstName(userInfo.getFirstName());
        user.setEmail(userInfo.getEmail());
        userRepository.save(user);
    }

}
