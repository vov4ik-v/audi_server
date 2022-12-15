package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.common.ApiResponse;
import com.volodymyrvasylyshyn.audi_server.dto.CarDto;
import com.volodymyrvasylyshyn.audi_server.dto.NewsDTO;
import com.volodymyrvasylyshyn.audi_server.dto.UserDTO;
import com.volodymyrvasylyshyn.audi_server.facade.NewsFacade;
import com.volodymyrvasylyshyn.audi_server.facade.UserFacade;
import com.volodymyrvasylyshyn.audi_server.model.AudiModel;
import com.volodymyrvasylyshyn.audi_server.model.Email;
import com.volodymyrvasylyshyn.audi_server.model.News;
import com.volodymyrvasylyshyn.audi_server.payload.MessageResponse;
import com.volodymyrvasylyshyn.audi_server.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class AdminController {
    private final UserService userService;
    private final CarService carService;
    private final EmailService emailService;
    private final AudiModelService audiModelService;
    private final NewsService newsService;
    private final NewsFacade newsFacade;
    private final UserFacade userFacade;
    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);
    private final EmailSenderService emailSenderService;

    public AdminController(UserService userService, CarService carService, EmailService emailService, AudiModelService audiModelService, NewsService newsService, NewsFacade newsFacade, UserFacade userFacade, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.carService = carService;
        this.emailService = emailService;
        this.audiModelService = audiModelService;
        this.newsService = newsService;
        this.newsFacade = newsFacade;
        this.userFacade = userFacade;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getCars(){
        List<CarDto> carList = carService.getAllCars();
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }
    @PostMapping("/addCar")
    public ResponseEntity<ApiResponse> addCar(@RequestBody CarDto carDto){
        AudiModel audiModel = audiModelService.getOneModel(carDto.getAudiModelId());
        carService.createCar(carDto,audiModel);
        return new ResponseEntity<>(new ApiResponse(true,"Added the car"), HttpStatus.CREATED);

    }
    @PutMapping("/editCar/{id}")
    public ResponseEntity<ApiResponse> editCar(@PathVariable("id") Integer carId,@RequestBody CarDto updatedCar){
        carService.editCar(carId,updatedCar);
        return new ResponseEntity<>(new ApiResponse(true,"Edited the car"), HttpStatus.OK);


    }
    @DeleteMapping("/deleteCar/{id}")
    public ResponseEntity<ApiResponse> deleteCar(@PathVariable("id") Integer carId){
        carService.deleteCar(carId);
        return new ResponseEntity<>(new ApiResponse(true,"Deleted the car"), HttpStatus.OK);

    }
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO> usersDTO = userService.getAllUser().stream().map(userFacade::userToUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }
    @PostMapping("/banUser/{userId}")
    public ResponseEntity<MessageResponse> banUser(@PathVariable Long userId){
        userService.banUser(userId);
        return new ResponseEntity<>(new MessageResponse("User with id: " + userId + " banned success"), HttpStatus.OK);

    }
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<MessageResponse> allFriends(@PathVariable Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(new MessageResponse("User with id: " + userId + " deleted success"), HttpStatus.OK);
    }
    @GetMapping("/news")
    public ResponseEntity<List<NewsDTO>> getNews(){
        List<NewsDTO> newsDTOList = newsService.getAllNews().stream().map(newsFacade::newsToNewsDTO).collect(Collectors.toList());
        return new ResponseEntity<>(newsDTOList,HttpStatus.OK);
    }
    @PostMapping("/createNews")
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO) throws IOException {
        News news = newsService.createNews(newsDTO);
        NewsDTO createdNews = newsFacade.newsToNewsDTO(news);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);

    }
    @PutMapping("/updateNews/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable("id") Long id, @RequestBody NewsDTO newsDTO){
        News news = newsService.updateNews(newsDTO,id);
        NewsDTO updatedNews= newsFacade.newsToNewsDTO(news);
        return new ResponseEntity<>(updatedNews,HttpStatus.OK);
    }
    @DeleteMapping("/deleteNews/{id}")
    public ResponseEntity<MessageResponse> deleteNews(@PathVariable("id") Long id){
        newsService.deletePost(id);
        return new ResponseEntity<>(new MessageResponse("news deleted success"),HttpStatus.OK);
    }
    @GetMapping("/emails")
    public ResponseEntity<List<Email>> getEmails(){
        List<Email> emails = emailService.getAll();
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }
    @PostMapping("/addEmail")
    public ResponseEntity<ApiResponse> addEmail(@RequestBody Email email){
        emailService.addEmail(email);
        String senderEmail = email.getEmail();
        try {
            emailSenderService.sendMail(senderEmail,"WELCOME","You are subscribe to are our newsletter");
        }
        catch (MailException e) {
            LOG.error("Error while sending out email ..{}", (Object) e.getStackTrace());
        }
        return new ResponseEntity<>(new ApiResponse(true,"Email added success"), HttpStatus.CREATED);
    }
    @DeleteMapping("/deleteEmail/{id}")
    public ResponseEntity<MessageResponse> deleteEmail(@PathVariable("id") Integer id){
        emailService.deleteEmail(id);
        return new ResponseEntity<>(new MessageResponse("email deleted success"),HttpStatus.OK);
    }

}
