package com.volodymyrvasylyshyn.audi_server.facade;


import com.volodymyrvasylyshyn.audi_server.dto.PostDTO;
import com.volodymyrvasylyshyn.audi_server.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {


        public PostDTO postToPostDTO(Post post){
            PostDTO postDTO = new PostDTO();
            postDTO.setUsername(post.getUser().getUsername());
            postDTO.setId(post.getId());
            postDTO.setCaption(post.getCaption());
            postDTO.setLikes(post.getLikes());
            postDTO.setUsersLiked(post.getLikedUsers());
            postDTO.setLocation(post.getLocation());
            postDTO.setTitle(post.getTitle());
            postDTO.setCreatedDate(post.getCreatedDate());
            postDTO.setPostImage(post.getPostImage());
            return postDTO;

        }
}

