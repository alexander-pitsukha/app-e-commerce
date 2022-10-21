package com.flatlogic.app.generator.security;

import com.flatlogic.app.generator.exception.UsernameNotFoundException;
import com.flatlogic.app.generator.repository.UserRepository;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
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
