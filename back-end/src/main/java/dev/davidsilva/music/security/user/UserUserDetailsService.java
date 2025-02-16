package dev.davidsilva.music.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameWithEagerlyFetchedPermissions(username);
        return userOptional.map(UserUserDetails::new)
                // TODO not sure if I just have to return null and some spring component up the filter chain will catch this ????
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
