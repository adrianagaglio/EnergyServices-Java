package epicode.it.energyservices.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AppUserRepo appUserRepo;
    @Override
    public UserDetails loadUserByUsername(String identifier)  {
        AppUser user = appUserRepo.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        String[] roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .toArray(String[]::new);


        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
