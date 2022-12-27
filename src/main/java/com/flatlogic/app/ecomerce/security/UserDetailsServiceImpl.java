package com.flatlogic.app.ecomerce.security;

import com.flatlogic.app.ecomerce.exception.UsernameNotFoundException;
import com.flatlogic.app.ecomerce.repository.UserRepository;
import com.flatlogic.app.ecomerce.util.Constants;
import com.flatlogic.app.ecomerce.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * UserDetailsService service.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCache userCache;
    private final MessageCodeUtil messageCodeUtil;

    /**
     * Load user by username.
     *
     * @param username User name
     * @return UserDetails
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        var userDetails = userCache.getUserFromCache(username);
        if (userDetails == null) {
            userDetails = Optional.ofNullable(userRepository.findByEmailAndEmailVerifiedIsTrue(username)).map(user ->
                            UserDetailsImpl.builder().user(user).build())
                    .orElseThrow(() -> new UsernameNotFoundException(messageCodeUtil
                            .getFullErrorMessageByBundleCode(Constants.ERROR_MSG_AUTH_INVALID_CREDENTIALS)));
            userCache.putUserInCache(userDetails);
        }
        return userDetails;
    }

}
