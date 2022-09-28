package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.dto.NewsDTO;
import com.volodymyrvasylyshyn.audi_server.facade.NewsFacade;
import com.volodymyrvasylyshyn.audi_server.model.ImageModel;
import com.volodymyrvasylyshyn.audi_server.model.News;
import com.volodymyrvasylyshyn.audi_server.model.Post;
import com.volodymyrvasylyshyn.audi_server.payload.MessageResponse;
import com.volodymyrvasylyshyn.audi_server.response.LikeNewsResponse;
import com.volodymyrvasylyshyn.audi_server.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/news")
@CrossOrigin
public class NewsController {
    private final NewsService newsService;
    private final NewsFacade newsFacade;

    public NewsController(NewsService newsService, NewsFacade newsFacade) {
        this.newsService = newsService;
        this.newsFacade = newsFacade;
    }

    @GetMapping("/all")
    public ResponseEntity<List<NewsDTO>> getAllNews(){
        List<NewsDTO> newsDTOList = newsService.getAllNews().stream().map(newsFacade::newsToNewsDTO).collect(Collectors.toList());
        return new ResponseEntity<>(newsDTOList,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable("id") Long id){
        News news = newsService.getNews(id);
        NewsDTO getNews = newsFacade.newsToNewsDTO(news);
        return new ResponseEntity<>(getNews,HttpStatus.OK);

    }
    @PostMapping("/create")
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO) throws IOException {
       News news = newsService.createNews(newsDTO);
       NewsDTO createdNews = newsFacade.newsToNewsDTO(news);
       return new ResponseEntity<>(createdNews, HttpStatus.CREATED);

    }
    @PostMapping("/update/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable("id") Long id, @RequestBody NewsDTO newsDTO){
        News news = newsService.updateNews(newsDTO,id);
        NewsDTO updatedNews= newsFacade.newsToNewsDTO(news);
        return new ResponseEntity<>(updatedNews,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteNews(@PathVariable("id") Long id){
        newsService.deletePost(id);
        return new ResponseEntity<>(new MessageResponse("news deleted success"),HttpStatus.OK);
    }

    @PostMapping("likeNews/{newsId}")
    public ResponseEntity<LikeNewsResponse> likeNews(@PathVariable("newsId") String newsId, Principal principal){
        News likedNews = newsService.likeNews(Long.parseLong(newsId),principal);
        return new ResponseEntity<>(new LikeNewsResponse(likedNews.getLikes(),likedNews.getLikedUsers()),HttpStatus.OK);

    }


}
