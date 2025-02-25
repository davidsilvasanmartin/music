package dev.davidsilva.music.security.auth;

import dev.davidsilva.music.security.user.User;
import dev.davidsilva.music.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbUserUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameWithEagerlyFetchedPermissions(username);
        return userOptional.map(DbUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
