package com.auth.auth.auth.configuration;
;

import com.auth.auth.models.Role;
import com.auth.auth.models.User;
import com.auth.auth.repositories.RoleRepository;

import com.auth.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleRepositoryInitializer {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    public RoleRepositoryInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostConstruct
    public void addAvailableRoles(){
        if (!roleRepository.findAll().isEmpty()) {
            return;
        }

        List<Role> roles = List.of(
                new Role(1, Role.RoleName.ROLE_ADMIN),

                new Role(3, Role.RoleName.ROLE_USER)
        );

        roleRepository.saveAll(roles);

        if (!userRepository.findAll().isEmpty()) {
            return;
        }

        var user = new User();
        user.setRoles(roleRepository.findAll().stream().collect(Collectors.toSet()));
        user.setEmail("admin@admin.com");
        user.setUsername("Admin");
        user.setPassword(encoder.encode("12345678"));

        userRepository.save(user);
    }
}
