package uexcel.com.ltts.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.exception.CustomException;
import uexcel.com.ltts.util.Repos;

@Service
public class CustomUserDetailsImp implements UserDetailsService {
    private final Repos repos;

    public CustomUserDetailsImp(Repos repos) {
        this.repos = repos;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = repos.getClientRepository().findByEmail(username);
        if (client == null) {
            throw new CustomException("Client not fund","404");
        }
        return client;
    }
}
