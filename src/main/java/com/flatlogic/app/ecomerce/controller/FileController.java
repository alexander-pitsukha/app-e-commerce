package com.flatlogic.app.ecomerce.controller;

import com.flatlogic.app.ecomerce.controller.request.MultipartRequest;
import com.flatlogic.app.ecomerce.exception.FileException;
import com.flatlogic.app.ecomerce.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileController REST controller.
 */
@Tag(name = "File controller", description = "File resources that provides access to available file data")
@RestController
@RequestMapping("file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    /**
     * Download file.
     *
     * @param privateUrl Private url
     * @return Resource
     */
    @Operation(summary = "Download file", description = "Provides downloading file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Resource",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    @GetMapping("download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String privateUrl) {
        log.info("Download file.");
        return ResponseEntity.ok(fileService.downloadFile(privateUrl));
    }

    /**
     * Upload products file.
     *
     * @param multipartRequest MultipartRequest
     * @param result           BindingResult
     * @return Void
     */
    @Operation(summary = "Upload products file", description = "Provides uploading products file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping("upload/products/image")
    public ResponseEntity<HttpStatus> uploadProductsFile(@Validated MultipartRequest multipartRequest, BindingResult result) {
        log.info("Upload products file.");
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            fileService.uploadProductsFile(multipartRequest.getFile(), multipartRequest.getFilename());
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Upload users file.
     *
     * @param multipartRequest MultipartRequest
     * @param result           BindingResult
     * @return Void
     */
    @Operation(summary = "Upload users file", description = "Provides uploading users file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return HttpStatus",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping("upload/users/avatar")
    public ResponseEntity<HttpStatus> uploadUsersFile(@Validated MultipartRequest multipartRequest, BindingResult result) {
        log.info("Upload users file.");
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            fileService.uploadUsersFile(multipartRequest.getFile(), multipartRequest.getFilename());
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * FileException handler.
     *
     * @param e FileException
     * @return Error message
     */
    @ExceptionHandler(FileException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleFileException(FileException e) {
        log.error("FileException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

}
