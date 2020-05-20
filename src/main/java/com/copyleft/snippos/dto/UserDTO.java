package com.copyleft.snippos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {

    @NotNull
    private String firstName;

    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;
}
