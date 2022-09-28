package com.volodymyrvasylyshyn.audi_server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CommentDTO {

    private Long id;
    @NotEmpty
    private String message;
    private String username;
    private LocalDateTime createdDate;
}
