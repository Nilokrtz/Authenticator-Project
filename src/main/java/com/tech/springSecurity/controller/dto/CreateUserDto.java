package com.tech.springSecurity.controller.dto;

public record CreateUserDto(String username, String password) {
    // Record class to hold user creation data
    // It automatically generates the constructor, getters, equals, hashCode, and toString methods
}