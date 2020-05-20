package com.copyleft.snippos.rest;

import com.copyleft.snippos.constants.GeneralConstants;
import com.copyleft.snippos.constants.StatusConstants;
import com.copyleft.snippos.domain.User;
import com.copyleft.snippos.dto.LoginDTO;
import com.copyleft.snippos.dto.UserDTO;
import com.copyleft.snippos.dto.response.StatusResponse;
import com.copyleft.snippos.security.TokenProvider;
import com.copyleft.snippos.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



    @GetMapping("/users")
    public StatusResponse<List<User>> getAllUsers() {
        logger.debug("Fetch all users from DB");

        return new StatusResponse(200, StatusConstants.SUCCESS, StatusConstants.SUCCESS, userService.findAll());
    }

    @PostMapping("/users/register")
    @Secured(GeneralConstants.ROLE_ADMIN)
    public StatusResponse<User> createUser(@RequestBody UserDTO userDTO) {
        logger.debug("create User");
        if (userService.findOneByEmail(userDTO.getEmail())!=null) {
            return new StatusResponse(400, StatusConstants.FAILED, StatusConstants.ALREADY_EXISTS, StatusConstants.ALREADY_EXISTS);
        } else {
            User newUser = userService.createUser(userDTO);
            return new StatusResponse(200, StatusConstants.SUCCESS, StatusConstants.SUCCESS, newUser);
        }
    }

    @PostMapping("/users/authenticate")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response, @RequestHeader("User-Agent") String userAgent) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginDTO.getRememberMe() == null) ? false : loginDTO.getRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(GeneralConstants.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(jwt);
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

}
