package com.divineaura.auth;

import com.divineaura.customer.CustomerDTO;

public record AuthenticationResponse(String token, CustomerDTO customerDTO) {
}
