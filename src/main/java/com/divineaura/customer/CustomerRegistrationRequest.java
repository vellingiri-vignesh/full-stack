package com.divineaura.customer;

public record CustomerRegistrationRequest (
    String name,
    String email,
    Integer age
){}

