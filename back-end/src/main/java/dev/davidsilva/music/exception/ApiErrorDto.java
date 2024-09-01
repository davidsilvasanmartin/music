package dev.davidsilva.music.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ApiErrorDto {
    private Date timestamp;
    private String message;
    private String details;
}
