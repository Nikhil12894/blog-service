package com.nk.blog.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nk.blog.dto.WebResponse;
import com.nk.blog.exception.BadRequestException;
import com.nk.blog.exception.CustomDataIntegrityViolationException;
import com.nk.blog.exception.DataConflictException;
import com.nk.blog.exception.DuplicateTransactionException;
import com.nk.blog.exception.ForbiddenException;
import com.nk.blog.exception.InternalServerException;
import com.nk.blog.exception.MethodNotAllowedException;
import com.nk.blog.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class TaskControllerAdvice {
    /**
     * Handles DuplicateConfigurationNameException and returns an HTTP
     * response with a message and status code of 409 (Conflict).
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateTransactionException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleDDuplicateTransactionException(DuplicateTransactionException ex) {
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Response for a bad request.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleBadRequestFormatException(BadRequestException ex) {
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        log.warn("Bad request received: {}", ex.getMessage());
        if (!ObjectUtils.isEmpty(ex.getErrors())) {
            response.setErrors(ex.getErrors());
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Response for a any server error.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleInternalServerException(InternalServerException ex) {
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        // Consider integrating with an alerting system to notify administrators
        // alertAdmin(ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Response for any data not found error.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleNotFoundException(NotFoundException ex) {
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Response for any method not allowed error.
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleMethodNotAllowedException(MethodNotAllowedException ex) {
        // This exception is thrown when an HTTP method is used that is not allowed for the endpoint
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Response for any forbidden error.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleForbiddenException(ForbiddenException ex) {
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    

    /**
     * Response for any data conflict.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataConflictException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleDataConflictException(DataConflictException ex) {
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

     /**
     * Response for any data conflict.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomDataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<WebResponse<Object>> handleDataIntegrityViolationException(CustomDataIntegrityViolationException ex) {
        WebResponse<Object> response = new WebResponse<>();
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
   
}