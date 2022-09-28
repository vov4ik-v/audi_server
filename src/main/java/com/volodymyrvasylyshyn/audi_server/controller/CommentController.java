package com.volodymyrvasylyshyn.audi_server.controller;


import com.volodymyrvasylyshyn.audi_server.dto.CommentDTO;
import com.volodymyrvasylyshyn.audi_server.facade.CommentFacade;
import com.volodymyrvasylyshyn.audi_server.model.Comment;
import com.volodymyrvasylyshyn.audi_server.payload.MessageResponse;
import com.volodymyrvasylyshyn.audi_server.service.CommentService;
import com.volodymyrvasylyshyn.audi_server.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO, @PathVariable("postId") String postId,
                                                BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO createdComment = commentFacade.commentToCommentDTO(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.OK);

    }
    @PostMapping("/news/{newsId}/create")
    public ResponseEntity<Object> createCommentToNews(@Valid @RequestBody CommentDTO commentDTO, @PathVariable("newsId") String newsId,
                                                BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveCommentToNews(Long.parseLong(newsId), commentDTO,principal);
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForNews(Long.parseLong(newsId)).stream()
                .map(commentFacade::commentToCommentDTO).collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);

    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId)).stream()
                .map(commentFacade::commentToCommentDTO).collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }
    @GetMapping("/newsComments/{newsId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToNews(@PathVariable("newsId") String newsId) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForNews(Long.parseLong(newsId)).stream()
                .map(commentFacade::commentToCommentDTO).collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }


    @PostMapping("/{commentId}/delete")

    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId){
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);


    }

}
