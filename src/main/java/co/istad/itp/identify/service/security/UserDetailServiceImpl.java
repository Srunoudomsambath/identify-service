package co.istad.itp.identify.service.security;

import co.istad.itp.identify.service.domain.Role;
import co.istad.itp.identify.service.domain.User;
import co.istad.itp.identify.service.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
// Spring security provide userdertail service

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    // UserDetails also an interface that provide username authentication and default boolean which return true
    // We get the data from database first by repository
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // we need to customize loggedInUsere cause it the child of the parent userdetail
        // Load a user from the database
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException(username));

        String[] roles = loggedInUser.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);


//         There are no permission only role
//        Set<GrantedAuthority> authorities = loggedInUser.getRoles()
//                .stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
//                .collect(Collectors.toSet());

        // we enable role and permission we need to map take permission and role
        List<GrantedAuthority> authorities = new ArrayList<>();
        loggedInUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        UserDetails userSecurity = org.springframework.security.core.userdetails.User.builder()
                .username(loggedInUser.getUsername())
                .password(loggedInUser.getPassword())
//                .roles(roles)
                .authorities(authorities)
                .build();
        log.info("UserDetailServiceImpl loadUserByUsername = {}",userSecurity.getAuthorities().toString());
        log.info("UserDetailServiceImpl loadUserByUsername = {}",userSecurity.getUsername());

        return userSecurity;
    }
}
