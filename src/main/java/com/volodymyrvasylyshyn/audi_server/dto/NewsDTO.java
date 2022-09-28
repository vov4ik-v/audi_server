package com.volodymyrvasylyshyn.audi_server.dto;

import com.volodymyrvasylyshyn.audi_server.model.ImageModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class NewsDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Integer likes;
    private byte[] newsImage;
    private LocalDateTime createdDate;
    private Set<String> usersLiked;

}
