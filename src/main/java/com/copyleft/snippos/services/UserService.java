package com.copyleft.snippos.services;

import com.copyleft.snippos.domain.Authority;
import com.copyleft.snippos.domain.User;
import com.copyleft.snippos.dto.UserDTO;
import com.copyleft.snippos.repository.AuthorityRepository;
import com.copyleft.snippos.repository.UserRepository;
import com.copyleft.snippos.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.copyleft.snippos.constants.GeneralConstants.ROLE_USER;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findOneByEmail(String email) {
        return userRepository.findOneByEmail(email).orElse(null);
    }

    public User createUser(UserDTO userDTO) {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findOneByName(ROLE_USER));
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .authorities(authorities)
                .createdDate(ZonedDateTime.now())
                .build();
        return this.save(user);
    }

    public Optional<User> findCurrentLoggedInUser() {
        return userRepository.findOneByEmail(SecurityUtil.getCurrentUserLogin());
    }

}
