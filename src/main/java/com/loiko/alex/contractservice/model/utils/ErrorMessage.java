package com.loiko.alex.contractservice.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private String message;
    private String code;
    private String detail;

    public ErrorMessage(String message, String detail) {
        super();
        this.message = message;
        this.detail = detail;
    }

    public ErrorMessage(String message) {
        this(message, "", "");
    }
}