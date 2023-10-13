package com.relex.relex_social.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class AppErrorDto {
    private final int status;
    private final String message;
    private Date timestamp = new Date();
}
