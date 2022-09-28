package com.volodymyrvasylyshyn.audi_server.facade;


import com.volodymyrvasylyshyn.audi_server.dto.CommentDTO;
import com.volodymyrvasylyshyn.audi_server.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

    public CommentDTO commentToCommentDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUsername(comment.getUsername());
        commentDTO.setMessage(comment.getMessage());
        commentDTO.setCreatedDate(comment.getCreatedDate());

        return commentDTO;
    }
}
