package com.copyleft.snippos.services;

import com.copyleft.snippos.domain.User;
import com.copyleft.snippos.repository.UserRepository;
import com.copyleft.snippos.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findCurrentLoggedInUser(){
        return userRepository.findOneByEmail(SecurityUtil.getCurrentUserLogin());
    }

}
