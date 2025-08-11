package com.tech.springSecurity.controller.dto;

public record LoginResponse(String acessToken, Long expiresIn) {
    // expiresIn is the time in seconds until the token expires
    
}
