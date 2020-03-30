package com.copyleft.snippos.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResponse<T> {

    private Integer code;

    private String status;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

}
