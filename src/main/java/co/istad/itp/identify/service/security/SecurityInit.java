package co.istad.itp.identify.service.security;


import co.istad.itp.identify.service.domain.Role;
import co.istad.itp.identify.service.domain.User;
import co.istad.itp.identify.service.features.role.RoleRepository;
import co.istad.itp.identify.service.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityInit {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setUsername("sambath");
            user.setPassword(passwordEncoder.encode("qwer"));
            user.setEmail("it.chhaya@gmail.com");
            user.setDob(LocalDate.of(1998, 9, 9));
            user.setGender("Male");
            user.setProfileImage("default_profile.jpg");
            user.setCoverImage("default_cover.jpg");
            user.setFamilyName("Chan");
            user.setGivenName("Chhaya");
            user.setPhoneNumber("077459947");
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setIsEnabled(true);

            // Assign role to user
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ADMIN"));
            roles.add(roleRepository.findByName("USER"));
            user.setRoles(roles);

            userRepository.save(user);
            log.info("User has been saved: {}", user.getId());
        }
    }
}
