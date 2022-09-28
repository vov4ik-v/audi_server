package com.volodymyrvasylyshyn.audi_server.facade;

import com.volodymyrvasylyshyn.audi_server.dto.NewsDTO;
import com.volodymyrvasylyshyn.audi_server.model.News;
import org.springframework.stereotype.Component;

@Component
public class NewsFacade {
    public NewsDTO newsToNewsDTO(News news){
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setCreatedDate(news.getCreatedDate());
        newsDTO.setDescription(news.getDescription());
        newsDTO.setTitle(news.getTitle());
        newsDTO.setLikes(news.getLikes());
        newsDTO.setId(news.getId());
        newsDTO.setNewsImage(news.getNewsImage());
        newsDTO.setStatus(news.getStatus());
        newsDTO.setUsersLiked(news.getLikedUsers());
        return newsDTO;
    }

}
