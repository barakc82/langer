package com.langer.server.security.services;

import com.langer.server.model.entity.impl.User;
import com.langer.server.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findFirstByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User Not Found with username: " + username);

        return new UserDetailsImpl(user.getId(), user.getUsername(), String.valueOf(user.getHash()));
    }
}