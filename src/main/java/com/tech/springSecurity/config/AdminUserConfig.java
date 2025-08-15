package com.tech.springSecurity.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.tech.springSecurity.entities.Role;
import com.tech.springSecurity.entities.User;
import com.tech.springSecurity.repository.RoleRepository;
import com.tech.springSecurity.repository.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner{
    
    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional // Garantir que as operações de banco de dados sejam executadas em uma transação
    public void run(String... args) throws Exception {
        
        var roleAdmin = roleRepository.findByName(Role.RoleName.ADMIN.name());

        if (roleAdmin == null) {
            System.out.println("Role ADMIN não encontrada no banco!");
            return;
        }
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
            user -> {
                System.out.println("admin já existe");
            },
            
            () -> {
                var user = new User();
                user.setUsername("admin"); 
                user.setPassword(bCryptPasswordEncoder.encode("123"));
                user.setRoles(Set.of(roleAdmin));
                userRepository.save(user);
            }
        );
    }
}
