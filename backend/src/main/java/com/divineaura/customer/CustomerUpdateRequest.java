package com.divineaura.customer;

public record CustomerUpdateRequest(
    String name,
    String email,
    Integer age
){}
