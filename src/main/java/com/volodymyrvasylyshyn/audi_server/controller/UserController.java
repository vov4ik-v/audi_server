package com.volodymyrvasylyshyn.audi_server.controller;



import com.volodymyrvasylyshyn.audi_server.dto.UserDTO;
import com.volodymyrvasylyshyn.audi_server.facade.UserFacade;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.payload.MessageResponse;
import com.volodymyrvasylyshyn.audi_server.service.UserService;
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

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {



    private final UserService userService;

    private final UserFacade userFacade;

    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){

        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("username") String username ){
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long userId ){
//        User user = userService.getUserById(userId);
//        UserDTO userDTO = userFacade.userToUserDTO(user);
//        return new ResponseEntity<>(userDTO, HttpStatus.OK);
//
//    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDTO,principal);
        UserDTO userUpdated = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
//    @PostMapping("/addToFriends")
//    public ResponseEntity<MessageResponse> addUserToFriends(@RequestBody Long userId, Principal principal){
////        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
////        if (!ObjectUtils.isEmpty(errors)) return errors;
//        userService.addUserToFriends(principal,userId);
//        return new ResponseEntity<>(new MessageResponse("User with id: " + userId + " added to friends success"), HttpStatus.OK);
//    }
//    @GetMapping("/allFriends")
//    public ResponseEntity<List<User>> allFriends(Principal principal){
//        List<User> friends = userService.getAllFriends(principal);
//        return new ResponseEntity<>(friends, HttpStatus.OK);
//    }
//    @DeleteMapping("/deleteFriend/{userId}")
//    public ResponseEntity<MessageResponse> allFriends(@PathVariable Long userId, Principal principal){
//        userService.deleteUserFromFriends(principal,userId);
//        return new ResponseEntity<>(new MessageResponse("User with id: " + userId + " deleted to friends success"), HttpStatus.OK);
//    }

}


