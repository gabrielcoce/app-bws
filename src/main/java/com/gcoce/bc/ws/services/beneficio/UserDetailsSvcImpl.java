package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.entities.beneficio.User;
import com.gcoce.bc.ws.entities.beneficio.UserDetailsImpl;
import com.gcoce.bc.ws.repositories.beneficio.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = "beneficioTransactionManager")
public class UserDetailsSvcImpl implements UserDetailsService {
    //@Autowired
    private UserRepository userRepository;

    public UserDetailsSvcImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}