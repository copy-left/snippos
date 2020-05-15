package com.copyleft.snippos.rest;

import com.copyleft.snippos.constants.StatusConstants;
import com.copyleft.snippos.domain.User;
import com.copyleft.snippos.dto.response.StatusResponse;
import com.copyleft.snippos.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public StatusResponse<List<User>> getAllUsers() {
        logger.debug("Fetch all users from DB");

        return new StatusResponse(200, StatusConstants.SUCCESS, StatusConstants.SUCCESS, userService.findAll());
    }

    @PostMapping("/users")
    public StatusResponse<User> createUser(@RequestBody User user) {
        logger.debug("Fetch all users from DB");
        return new StatusResponse(200, StatusConstants.SUCCESS, StatusConstants.SUCCESS, userService.save(user));
    }

}
