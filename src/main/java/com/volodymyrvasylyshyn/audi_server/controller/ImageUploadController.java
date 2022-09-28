package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.model.ImageModel;
import com.volodymyrvasylyshyn.audi_server.payload.MessageResponse;
import com.volodymyrvasylyshyn.audi_server.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageUploadController {
    @Autowired
    private ImageUploadService imageUploadService;
    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        imageUploadService.uploadImageToUser(file, principal);
        return new ResponseEntity<>(new MessageResponse("Image Uploaded Successfully"), HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        imageUploadService.uploadImageToPost(file, principal, Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Image Uploaded Successfully"),HttpStatus.OK);

    }
    @PostMapping("/newsUpload/{newsId}")
    public ResponseEntity<MessageResponse> uploadImageToNews(@PathVariable("newsId") String newsId, @RequestParam("file") MultipartFile file) throws IOException {
        imageUploadService.uploadImageToNews(file, Long.parseLong(newsId));
        return new ResponseEntity<>(new MessageResponse("Image Uploaded Successfully"),HttpStatus.OK);

    }
    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageToUser(Principal principal) throws DataFormatException {
        ImageModel userImage = imageUploadService.getImageToUser(principal);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }
    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModel> getImageToPost(@PathVariable("postId") String postId) throws DataFormatException {
        ImageModel postImage = imageUploadService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }
    @GetMapping("/newsImage/{newsId}")
    public ResponseEntity<ImageModel> getImageToNews(@PathVariable("newsId") String newsId) throws DataFormatException {
        ImageModel newsImage = imageUploadService.getImageToNews(Long.parseLong(newsId));
        return new ResponseEntity<>(newsImage, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ImageModel> getImageToUserByUsername(@PathVariable("username") String username) throws DataFormatException {
        ImageModel postImage = imageUploadService.getImageToUserByUsername(username);
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }




}
