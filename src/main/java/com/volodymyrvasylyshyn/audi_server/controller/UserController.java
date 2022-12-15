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
import java.util.stream.Collectors;

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
    @GetMapping("/allUser")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> usersDTO = userService.getAllUser().stream().map(userFacade::userToUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }


    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("username") String username ){
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }
    @GetMapping("/byId/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long userId ){
        User user = userService.getUserById(userId);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDTO,principal);
        UserDTO userUpdated = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
         userService.forgotPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/forgot/setNewPassword")
    public ResponseEntity<?> setNewPassword(@RequestParam String resetToken,@RequestParam String password){
        User user = userService.getByResetToken(resetToken);
         userService.updatePassword(user,password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/changePassword")
    public ResponseEntity<MessageResponse> changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal){
        boolean isChange = userService.changePassword(oldPassword,newPassword,principal);
        if (isChange){
            return new ResponseEntity<>(new MessageResponse("Password changed success"),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new MessageResponse("Password not changed"),HttpStatus.BAD_REQUEST);
        }

    }


}


