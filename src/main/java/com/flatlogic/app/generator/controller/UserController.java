package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.controller.request.UserRequest;
import com.flatlogic.app.generator.dto.UserDto;
import com.flatlogic.app.generator.entity.User;
import com.flatlogic.app.generator.service.UserService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * UserController REST controller.
 */
@Tag(name = "User controller", description = "User resources that provides access to available user data")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final DefaultConversionService defaultConversionService;

    /**
     * Get users.
     *
     * @param modelAttribute GetModelAttribute
     * @return User RowsWrapper
     */
    @Operation(summary = "Get users", description = "Provides all available user list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RowsData<UserDto>",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping
    public ResponseEntity<RowsData<UserDto>> getUsers(@ModelAttribute GetModelAttribute modelAttribute) {
        log.info("Get users.");
        RowsData<UserDto> rowsData = new RowsData<>();
        List<User> users = userService.getUsers(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<UserDto> userDtos = users.stream().map(user -> defaultConversionService.convert(user,
                UserDto.class)).collect(Collectors.toList());
        rowsData.setRows(userDtos);
        rowsData.setCount(userDtos.size());
        return ResponseEntity.ok(rowsData);
    }

    /**
     * Get users.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Users
     */
    @Operation(summary = "Get users (autocomplete)",
            description = "Provides all available user list (autocomplete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return RowsData<AutocompleteData>",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getUsersAutocomplete(@RequestParam String query,
                                                                       @RequestParam Integer limit) {
        log.info("Get users (autocomplete).");
        List<User> users = userService.getUsers(query, limit);
        List<AutocompleteData> wrappers = users.stream().map(user ->
                new AutocompleteData(user.getId(), user.getEmail())).collect(Collectors.toList());
        return ResponseEntity.ok(wrappers);
    }

    /**
     * Get user by id.
     *
     * @param id User Id
     * @return User
     */
    @Operation(summary = "Get user by id", description = "Provides available user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        log.info("Get user by  id = {}.", id);
        return Optional.ofNullable(userService.getUserById(id))
                .map(user -> ResponseEntity.ok(defaultConversionService.convert(user, UserDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save user.
     *
     * @param requestData RequestData
     * @return User
     */
    @Operation(summary = "Save user", description = "Provides saving user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return UserDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody RequestData<UserRequest> requestData) {
        log.info("Save user.");
        var user = userService.saveUser(requestData.getData());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                        .buildAndExpand(user.getId()).toUri())
                .body(defaultConversionService.convert(user, UserDto.class));
    }

    /**
     * Update user.
     *
     * @param id          User id
     * @param requestData RequestData
     * @return User
     */
    @Operation(summary = "Update user", description = "Provides updating user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return UserDto",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id,
                                              @RequestBody RequestData<UserRequest> requestData) {
        log.info("Update user id = {}.", id);
        var user = userService.updateUser(id, requestData.getData());
        return ResponseEntity.ok(defaultConversionService.convert(user, UserDto.class));
    }

    /**
     * Delete user.
     *
     * @param id User id
     * @return Void
     */
    @Operation(summary = "Delete user", description = "Provides deleting user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable UUID id) {
        log.info("Delete user id = {}.", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
