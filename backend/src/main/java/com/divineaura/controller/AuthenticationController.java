package com.divineaura.controller;

import com.divineaura.auth.AuthenticationRequest;
import com.divineaura.auth.AuthenticationResponse;
import com.divineaura.auth.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginRequest){
        AuthenticationResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, response.token())
            .body(response);
    }
}
