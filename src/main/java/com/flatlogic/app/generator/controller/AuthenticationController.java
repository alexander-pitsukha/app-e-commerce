package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.request.AuthRequest;
import com.flatlogic.app.generator.controller.request.ResetPasswordRequest;
import com.flatlogic.app.generator.controller.request.SendEmailRequest;
import com.flatlogic.app.generator.controller.request.UpdatePasswordRequest;
import com.flatlogic.app.generator.controller.request.VerifyEmailRequest;
import com.flatlogic.app.generator.dto.UserDto;
import com.flatlogic.app.generator.exception.SendMailException;
import com.flatlogic.app.generator.exception.UsernameNotFoundException;
import com.flatlogic.app.generator.jwt.JwtTokenUtil;
import com.flatlogic.app.generator.service.UserService;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.NoSuchElementException;

/**
 * AuthenticationController REST controller.
 */
@Tag(name = "Authentication controller", description = "Provides access to authentication")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final DefaultConversionService defaultConversionService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserCache userCache;
    private final MessageCodeUtil messageCodeUtil;

    /**
     * Get current user.
     *
     * @param userDetails UserDto
     * @return UserDetails
     */
    @Operation(summary = "Get current user", description = "Provides available current user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @GetMapping("me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Get current user.");
        var user = userService.getUserByEmail(userDetails.getUsername());
        var userDto = defaultConversionService.convert(user, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Local login method.
     *
     * @param authRequest AuthRequest
     * @return JWT token
     */
    @Operation(summary = "Local login", description = "Provides local login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return String",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @PostMapping("signin/local")
    public ResponseEntity<String> localLogin(@RequestBody @Validated AuthRequest authRequest) {
        log.info("Login method.");
        userCache.removeUserFromCache(authRequest.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));
        return ResponseEntity.ok(jwtTokenUtil.generateToken(authRequest.getEmail()));
    }

    /**
     * Google sign in.
     *
     * @return RedirectView
     */
    @Operation(summary = "Google sign in", description = "Provides google sign in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RedirectView",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @GetMapping("signin/google")
    public RedirectView signInGoogle() {
        log.info("Google sign in.");
        return new RedirectView("/api/oauth2/authorization/google");
    }

    /**
     * Sign up.
     *
     * @param authRequest AuthRequest
     * @return JWT token
     */
    @Operation(summary = "Sign up", description = "Provides sign up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return String",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @PostMapping("signup")
    public ResponseEntity<String> signUp(@RequestBody @Validated AuthRequest authRequest) {
        log.info("Sign up.");
        userService.createUserAndSendEmail(authRequest.getEmail(), authRequest.getPassword());
        return ResponseEntity.ok(jwtTokenUtil.generateToken(authRequest.getEmail()));
    }

    /**
     * Verify email.
     *
     * @param verifyEmailRequest VerifyEmailRequest
     * @return Void
     */
    @Operation(summary = "Verify email", description = "Provides verifying email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @PutMapping("verify-email")
    public ResponseEntity<HttpStatus> verifyEmail(@RequestBody @Validated VerifyEmailRequest verifyEmailRequest) {
        log.info("Verify email.");
        userService.updateEmailVerification(verifyEmailRequest.getToken());
        return ResponseEntity.ok().build();
    }

    /**
     * Update user password.
     *
     * @param passwordRequest UpdatePasswordRequest
     * @param userDetails     UserDetails
     * @return Void
     */
    @Operation(summary = "Update user password", description = "Provides updating user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @PutMapping("password-update")
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody @Validated UpdatePasswordRequest passwordRequest,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Update user password.");
        userService.updateUserPassword(userDetails.getUsername(), passwordRequest.getCurrentPassword(),
                passwordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * Send email for reset password.
     *
     * @param sendEmailRequest SendEmailRequest
     * @return Void
     */
    @Operation(summary = "Send email for reset password", description = "Provides sending email for reset password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @PostMapping("send-password-reset-email")
    public ResponseEntity<HttpStatus> sendEmailForResetPassword(@RequestBody @Validated SendEmailRequest sendEmailRequest) {
        log.info("Send email for reset password.");
        var user = userService.getUserByEmail(sendEmailRequest.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_BY_EMAIL_NOT_FOUND, new Object[]{sendEmailRequest.getEmail()}));
        }
        userService.updateUserPasswordResetTokenAndSendEmail(sendEmailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * Reset password.
     *
     * @param resetPasswordRequest ResetPasswordRequest
     * @return UserDto
     */
    @Operation(summary = "Reset password", description = "Provides reset password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @PutMapping("password-reset")
    public ResponseEntity<UserDto> resetPassword(@RequestBody @Validated ResetPasswordRequest resetPasswordRequest) {
        log.info("Reset password.");
        var user = userService.updateUserPasswordByPasswordResetToken(resetPasswordRequest.getToken(),
                resetPasswordRequest.getPassword());
        return ResponseEntity.ok(defaultConversionService.convert(user, UserDto.class));
    }

    /**
     * BadCredentialsException handler.
     *
     * @param e BadCredentialsException
     * @return Error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleInvalidLoginException(BadCredentialsException e) {
        log.error("BadCredentialsException handler.", e);
        return new ResponseEntity<>(messageCodeUtil.getFullErrorMessageByBundleCode(
                Constants.ERROR_MSG_AUTH_INVALID_CREDENTIALS), HttpStatus.UNAUTHORIZED);
    }

    /**
     * UsernameNotFoundException handler.
     *
     * @param e UsernameNotFoundException
     * @return Error message
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("UsernameNotFoundException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * NoSuchElementException handler.
     *
     * @param e NoSuchElementException
     * @return Error message
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        log.error("NoSuchElementException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * SendMailException handler.
     *
     * @param e SendMailException
     * @return Error message
     */
    @ExceptionHandler(SendMailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleSendMailException(SendMailException e) {
        log.error("SendMailException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

}
