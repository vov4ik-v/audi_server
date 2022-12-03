package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.dto.UserDTO;
import com.volodymyrvasylyshyn.audi_server.facade.UserFacade;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import com.volodymyrvasylyshyn.audi_server.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class FriendController {
    private final UserRepository userRepository;
    private final FriendService friendService;

     private  final UserFacade userFacade;

    public FriendController(UserRepository userRepository, FriendService friendService, UserFacade userFacade) {
        this.userRepository = userRepository;

        this.friendService = friendService;
        this.userFacade = userFacade;
    }

    @PostMapping("addFriend")
    public ResponseEntity<?> addUser(@RequestParam("friendId")String friendId, Principal principal) throws NullPointerException{
        User user = getUserByPrincipal(principal);
        UserDTO currentUser = userFacade.userToUserDTO(user);
        friendService.saveFriend(currentUser,Integer.parseInt(friendId));
        return ResponseEntity.ok("Friend added successfully");
    }

    @GetMapping("listFriends")
    public ResponseEntity<List<UserDTO>> getFriends(Principal principal) {
        List<User> myFriends = friendService.getFriends(principal);
        List<UserDTO> userDTO = myFriends.stream().map(userFacade::userToUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("isFriend")
    public ResponseEntity<Boolean> checkToFriends(@RequestParam("friendId")String friendId, Principal principal){
        Boolean isFriend = friendService.checkToFriends(friendId,principal);
        return new ResponseEntity<>(isFriend,HttpStatus.OK);

    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }
}
