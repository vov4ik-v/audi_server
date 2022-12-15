package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.dto.UserDTO;
import com.volodymyrvasylyshyn.audi_server.facade.UserFacade;
import com.volodymyrvasylyshyn.audi_server.model.Friend;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.FriendRepository;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository, UserFacade userFacade) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.userFacade = userFacade;
    }
    private final UserFacade userFacade;

    public void saveFriend(Principal principal,long id){
        User currentUser = getUserByPrincipal(principal);
        UserDTO currentUserDTO = userFacade.userToUserDTO(currentUser);

        User user = userRepository.findUserById(id).orElse(null);
        UserDTO userDto2 = userFacade.userToUserDTO(user);

        Friend friend = new Friend();
        User user1 = userRepository.findUserByUsername(currentUserDTO.getUsername()).orElse(null);
        User user2 = userRepository.findUserByUsername(userDto2.getUsername()).orElse(null);
        User firstuser = user1;
        User seconduser = user2;

//        if(user1.getId() > user2.getId()){
//            firstuser = user2;
//            seconduser = user1;
//        }
        if( !(friendRepository.existsByFirstUserAndSecondUser(firstuser,seconduser)) ){
            friend.setCreatedDate(new Date());
            friend.setFirstUser(firstuser);
            friend.setSecondUser(seconduser);
            friendRepository.save(friend);
        }
        else{
            friend = friendRepository.findByFirstUserAndSecondUser(firstuser,seconduser);
            friendRepository.delete(friend);
        }

    }
    public List<User> getFollowing(String username){

        User currentUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
//        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();
        for (Friend friend : friendsByFirstUser) {
            friendUsers.add(userRepository.findUserById(friend.getSecondUser().getId()).orElse(null));
        }
//        for (Friend friend : friendsBySecondUser) {
//            friendUsers.add(userRepository.findUserById(friend.getFirstUser().getId()).orElse(null));
//        }
        return friendUsers;

    }
    public List<User> getFollowers(String username){

        User currentUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
//        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();
        for (Friend friend : friendsBySecondUser) {
            friendUsers.add(userRepository.findUserById(friend.getFirstUser().getId()).orElse(null));
        }
//        for (Friend friend : friendsBySecondUser) {
//            friendUsers.add(userRepository.findUserById(friend.getFirstUser().getId()).orElse(null));
//        }
        return friendUsers;

    }

    public Boolean checkToFriends(String friendId, String username) {
        List<User> friends = getFollowing(username);
        return friends.stream().anyMatch((friend) -> friend.getId() == Long.parseLong(friendId));
    }
    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    public Integer getCountFollowing(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found "));
        return friendRepository.findByFirstUser(user).size();


    }

    public Integer getCountFollowers(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found "));
        return friendRepository.findBySecondUser(user).size();

    }

//    private boolean isFriend(Friend firstUser,Friend secondUser){
//
//    }


}
