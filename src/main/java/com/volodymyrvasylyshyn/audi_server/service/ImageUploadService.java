package com.volodymyrvasylyshyn.audi_server.service;


import com.volodymyrvasylyshyn.audi_server.exeptions.ImageNotFoundException;
import com.volodymyrvasylyshyn.audi_server.exeptions.NewsNotFoundException;
import com.volodymyrvasylyshyn.audi_server.model.ImageModel;
import com.volodymyrvasylyshyn.audi_server.model.News;
import com.volodymyrvasylyshyn.audi_server.model.Post;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.ImageModelRepository;
import com.volodymyrvasylyshyn.audi_server.repository.NewsRepository;
import com.volodymyrvasylyshyn.audi_server.repository.PostRepository;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageModelRepository imageRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public ImageUploadService(PostRepository postRepository, UserRepository userRepository, ImageModelRepository imageRepository, NewsRepository newsRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.newsRepository = newsRepository;
    }
//    public void uploadImageToNews(MultipartFile file, Long newsId) {
//        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException("news not found"));
//        LOG.info("Uploading iamge to news {}", news.getTitle());
//        ImageModel userProfileImage = imageRepository.findByNewsId(news.getId()).orElse(null);
//        if (!ObjectUtils.isEmpty(userProfileImage)) {
//            imageRepository.delete(userProfileImage);
//        }
//        ImageModel imageModel = new ImageModel();
//        imageModel.setUserId(user.getId());
//        imageModel.setImageBytes(compressBytes(file.getBytes()));
//        imageModel.setName(file.getOriginalFilename());
//        return imageRepository.save(imageModel);
//
//    }
    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOG.info("Uploading iamge frofile to user {}", user.getUsername());
        ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);


    }

    public ImageModel uploadImageToNews(MultipartFile file, Long newsId) throws IOException {
        ImageModel newsImage = imageRepository.findByNewsId(newsId).orElse(null);
        if (!ObjectUtils.isEmpty(newsImage)) {
            imageRepository.delete(newsImage);
        }
        ImageModel imageModel = new ImageModel();
        imageModel.setNewsId(newsId);
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        if (newsImage != null) {
            newsImage.setImageBytes(imageModel.getImageBytes());
        }
        return imageRepository.save(imageModel);





    }

    public ImageModel uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts().stream().filter(p -> p.getId().equals(postId)).collect(toSinglePostCollector());
        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(postId);
        imageModel.setImageBytes(file.getBytes());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        LOG.info("Uploading image to Post {}", post.getId());
        return imageRepository.save(imageModel);


    }



    public ImageModel getImageToUser(Principal principal) throws DataFormatException {
        User user = getUserByPrincipal(principal);
        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
        if(!ObjectUtils.isEmpty(imageModel)){

            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));

        }
        return imageModel;

    }
    public ImageModel getImageToUserByUsername(String username) throws DataFormatException {
        User user = userRepository.findUserByUsername(username) .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
        if(!ObjectUtils.isEmpty(imageModel)){
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;

    }
    public ImageModel getImageToPost(Long postId) throws DataFormatException {
        ImageModel imageModel = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: " + postId));

        if(!ObjectUtils.isEmpty(imageModel)){

            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));

        }

        return imageModel;

    }
    public ImageModel getImageToNews(Long newsId) throws DataFormatException {
        ImageModel imageModel = imageRepository.findByNewsId(newsId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to News: " + newsId));

        if(!ObjectUtils.isEmpty(imageModel)){

            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));

        }
        return imageModel;
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

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );

    }



}
