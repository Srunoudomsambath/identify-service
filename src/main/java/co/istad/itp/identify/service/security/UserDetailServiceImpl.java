package co.istad.itp.identify.service.security;

import co.istad.itp.identify.service.domain.User;
import co.istad.itp.identify.service.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException(username));

        UserDetails userSecurity = org.springframework.security.core.userdetails.User.builder()
                .username(loggedInUser.getUsername())
                .password(loggedInUser.getPassword())
                .build();
        log.info("UserDetailServiceImpl loadUserByUsername = {}",userSecurity.getUsername());

        return userSecurity;
    }
}
