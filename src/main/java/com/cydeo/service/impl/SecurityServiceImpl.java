package com.cydeo.service.impl;

import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;
    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override// return type UserDetails -  Spring User that implements UserDetail interface in UserPrinciple class
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepository.findByUserNameAndIsDeleted(username, false);
        if(user==null){throw new UsernameNotFoundException(username);}
        return new UserPrincipal(user);
        // get user from DB and convert to Spring User through userPrincipal
    }
}

