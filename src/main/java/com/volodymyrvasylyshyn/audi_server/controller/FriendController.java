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
    private final FriendService friendService;

     private  final UserFacade userFacade;

    public FriendController( FriendService friendService, UserFacade userFacade) {
        this.friendService = friendService;
        this.userFacade = userFacade;
    }

    @PostMapping("addFriend")
    public ResponseEntity<?> addUser(@RequestParam("friendId")String friendId, Principal principal) throws NullPointerException{
        friendService.saveFriend(principal,Integer.parseInt(friendId));
        return ResponseEntity.ok("Friend added successfully");
    }

    @GetMapping("listFollowing/{username}")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable("username") String username) {
        List<User> myFriends = friendService.getFollowing(username);
        List<UserDTO> userDTO = myFriends.stream().map(userFacade::userToUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @GetMapping("listFollowers/{username}")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable("username") String username) {
        List<User> myFriends = friendService.getFollowers(username);
        List<UserDTO> userDTO = myFriends.stream().map(userFacade::userToUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @GetMapping("countFollowing/{username}")
    public ResponseEntity<Integer> getCountFollowing(@PathVariable("username") String username){
        Integer countFollowing = friendService.getCountFollowing(username);
        return new ResponseEntity<>(countFollowing,HttpStatus.OK);

    }
    @GetMapping("countFollowers/{username}")
    public ResponseEntity<Integer> getCountFollowers(@PathVariable("username") String username){
        Integer countFollowers = friendService.getCountFollowers(username);
        return new ResponseEntity<>(countFollowers,HttpStatus.OK);

    }


    @GetMapping("isFriend/{currentUserUsername}/{friendId}")
    public ResponseEntity<Boolean> checkToFriends(@PathVariable("friendId")String friendId, @PathVariable("currentUserUsername") String username){
        Boolean isFriend = friendService.checkToFriends(friendId,username);
        return new ResponseEntity<>(isFriend,HttpStatus.OK);

    }

}
