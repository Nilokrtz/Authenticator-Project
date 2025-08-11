package com.tech.springSecurity.controller.dto;

public record LoginRequest(String username, String password) {
    // Record class to hold login request data
    // It automatically generates the constructor, getters, equals, hashCode, and toString methods
    
}
