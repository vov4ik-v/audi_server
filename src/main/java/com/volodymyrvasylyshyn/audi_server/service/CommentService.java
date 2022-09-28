package com.volodymyrvasylyshyn.audi_server.service;



import com.volodymyrvasylyshyn.audi_server.dto.CommentDTO;
import com.volodymyrvasylyshyn.audi_server.exeptions.NewsNotFoundException;
import com.volodymyrvasylyshyn.audi_server.model.Comment;
import com.volodymyrvasylyshyn.audi_server.model.News;
import com.volodymyrvasylyshyn.audi_server.model.Post;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.CommentRepository;
import com.volodymyrvasylyshyn.audi_server.repository.NewsRepository;
import com.volodymyrvasylyshyn.audi_server.repository.PostRepository;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public CommentService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, NewsRepository newsRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
    }


    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findById(postId).orElseThrow(() -> new UsernameNotFoundException("Post connot be found for username: " + user.getEmail()));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());
        LOG.info("Saving comment for Post : {}", post.getId());
        return commentRepository.save(comment);

    }
    public Comment saveCommentToNews(Long newsId, CommentDTO commentDTO,Principal principal) {
        User user = getUserByPrincipal(principal);
        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException("News with id:" + newsId+ " not found"));
        Comment comment = new Comment();
        comment.setNews(news);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());
        LOG.info("Saving comment for News : {}", news.getId());
        return commentRepository.save(comment);

    }

    public List<Comment> getAllCommentsForPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new UsernameNotFoundException("Post connot be found for"));
        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments;
    }

    public List<Comment> getAllCommentsForNews(Long newsId){
        News news = newsRepository.findById(newsId).orElseThrow(() -> new UsernameNotFoundException("Post connot be found for"));
        List<Comment> comments = commentRepository.findAllByNews(news);
        return comments;
    }

    public void deleteComment(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);

    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }


}
