package com.divineaura.controller;

import com.divineaura.auth.AuthenticationRequest;
import com.divineaura.auth.AuthenticationResponse;
import com.divineaura.auth.AuthenticationService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<Void> login(@RequestBody AuthenticationRequest loginRequest){
        AuthenticationResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, response.token())
            .build();
    }

}
