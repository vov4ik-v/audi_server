package com.volodymyrvasylyshyn.audi_server.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String caption;
    private String location;
    private String username;
    private Integer likes;
    private byte[] postImage;
    private LocalDateTime createdDate;
    private Set<String> usersLiked;

}
