package com.loiko.alex.contractservice.model.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@AllArgsConstructor
@Getter
@Setter
public class ResponseWrapper {

    private Object data;
    private Object metadata;
    private List<ErrorMessage> errors;
}