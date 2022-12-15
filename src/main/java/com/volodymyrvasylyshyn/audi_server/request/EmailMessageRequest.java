package com.volodymyrvasylyshyn.audi_server.request;

import lombok.Data;

@Data
public class EmailMessageRequest {
    private String subject;
    private String message;
}
