package com.interswitch.dps.codemanagement.utils;

import org.springframework.validation.BindingResult;
import java.util.stream.Collectors;

public class ErrorResponseManager {
    public static String getErrorMessages(BindingResult result) {
        return result.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
    }
}