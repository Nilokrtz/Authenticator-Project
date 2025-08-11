package com.tech.springSecurity.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.RestController;

import com.tech.springSecurity.controller.dto.LoginRequest;
import com.tech.springSecurity.controller.dto.LoginResponse;
import com.tech.springSecurity.repository.UserRepository;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.extern.java.Log;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;

    private final UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        
        var user = userRepository.findByUsername(loginRequest.username());
        
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("user or password is invalid");
        }

        var now = Instant.now();
        var expiresIn = 300L; 

        // Criando Token JWT
        var claims = JwtClaimsSet.builder()
                .issuer("Nilokrtz") // Define o emissor do token
                .subject(user.get().getId().toString()) // Define o assunto do token
                .issuedAt(now) // Define a data de emissão do token
                .expiresAt(now.plusSeconds(expiresIn)) // Define a data de expiração do token
                .build();
        
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(); // Codifica os claims para gerar o token JWT

        return ResponseEntity.ok(new LoginResponse(jwtValue,expiresIn));
    }
    

    
}