package uexcel.com.ltts.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.exception.CustomException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RepositoryService repositoryService;

    public UserDetailsServiceImpl(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = repositoryService.getClientRepository().findByEmail(username);
        if (client == null) {
            throw new CustomException("Client not fund","404");
        }
        return client;
    }
}
