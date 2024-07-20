package com.nk.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "")
public class HealthController {

    /**
     * Route for readiness probe
     * @return httpStatus ok
     */
    @GetMapping("/healthz")
    public ResponseEntity<Void> healthz(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}