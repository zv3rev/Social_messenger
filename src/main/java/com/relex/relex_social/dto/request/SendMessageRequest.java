package com.relex.relex_social.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendMessageRequest {
    @NotBlank(message = "Message can't be blank")
    private String message;
}
