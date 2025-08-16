package com.tech.springSecurity.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tech.springSecurity.controller.dto.CreateUserDto;
import com.tech.springSecurity.entities.Role;
import com.tech.springSecurity.entities.User;
import com.tech.springSecurity.repository.RoleRepository;
import com.tech.springSecurity.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.experimental.var;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class UserController {
    
    private final UserRepository userRepository;    

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public UserController(UserRepository userRepository , RoleRepository roleRepository , BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
        
        var basicRole = roleRepository.findByName(Role.RoleName.BASIC.name());

        var userFromDb = userRepository.findByUsername(dto.username());

        if(userFromDb.isPresent()) {
           throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build(); // Retorna 200 OK se o usuário for criado com sucesso
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") // Permite acesso se o usuário tiver o escopo 'admin'    
    public ResponseEntity<List<User>> ListUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users); // Retorna a lista de usuários
    }
    
    
}
