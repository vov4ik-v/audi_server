package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.dto.NewsDTO;
import com.volodymyrvasylyshyn.audi_server.exeptions.NewsNotFoundException;
import com.volodymyrvasylyshyn.audi_server.model.ImageModel;
import com.volodymyrvasylyshyn.audi_server.model.News;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.ImageModelRepository;
import com.volodymyrvasylyshyn.audi_server.repository.NewsRepository;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class NewsService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final ImageModelRepository imageRepository;

    public NewsService(NewsRepository newsRepository, UserRepository userRepository, ImageModelRepository imageRepository) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public List<News> getAllNews(){
      return newsRepository.findAllByOrderByCreatedDateDesc();

    }

    public News getNews(Long id){
        return getNewsById(id);
    }

    public News createNews(NewsDTO newsDTO) throws IOException {
        News news = new News();
        news.setDescription(newsDTO.getDescription());
        news.setTitle(newsDTO.getTitle());
        news.setLikes(0);
        news.setStatus(newsDTO.getStatus());
        news.setNewsImage(newsDTO.getNewsImage());
        LOG.info("Saving news with title : {}",newsDTO.getTitle() );
        return newsRepository.save(news);
    }

    public News updateNews(NewsDTO newsDTO,Long id){
        News news = getNewsById(id);
        news.setStatus(newsDTO.getStatus());
        if(newsDTO.getLikes() != null) {
            news.setLikes(newsDTO.getLikes());
        }
        news.setDescription(newsDTO.getDescription());
        news.setTitle(newsDTO.getTitle());
        news.setNewsImage(newsDTO.getNewsImage());
        return newsRepository.save(news);
    }

    public News likeNews(Long newsId,Principal principal) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException("news with id:" + newsId+" not found"));
        String username = getUserByPrincipal(principal).getUsername();
        Optional<String> userLiked = news.getLikedUsers().stream().filter(u -> u.equals(username)).findAny();
        if(userLiked.isPresent()){
            news.setLikes(news.getLikes()-1);
            news.getLikedUsers().remove(username);
        }
        else{
            news.setLikes(news.getLikes()+1);
            news.getLikedUsers().add(username);
        }
        newsRepository.save(news);
        return news;
    }

    public void deletePost(Long newsId){
        News news = getNewsById(newsId);
        Optional<ImageModel> imageModel = imageRepository.findByNewsId(news.getId());
        newsRepository.delete(news);
        imageModel.ifPresent(imageRepository::delete);
    }

    private News getNewsById(Long id){
        return newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException("news with id:"+ id + " not found"));

    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);

        }

        try {
            outputStream.close();
        } catch (Exception e) {
            LOG.error("Connot compress Bytes");

        }
//        System.out.println("Compressed image Bytes Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();


    }

    private static byte[] decompressBytes(byte[] data) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);

            }
            outputStream.close();
        } catch (Exception e) {
            LOG.error("Connot decompress Bytes");

        }
//        System.out.println("Decompressed image Bytes Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();


    }



    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

}
