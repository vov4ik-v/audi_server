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

    public void saveFriend(UserDTO userDto1,long id){
        User user = userRepository.findUserById(id).orElse(null);
        UserDTO userDto2 = userFacade.userToUserDTO(user);

        Friend friend = new Friend();
        User user1 = userRepository.findUserByUsername(userDto1.getUsername()).orElse(null);
        User user2 = userRepository.findUserByUsername(userDto2.getUsername()).orElse(null);
        User firstuser = user1;
        User seconduser = user2;

        if(user1.getId() > user2.getId()){
            firstuser = user2;
            seconduser = user1;
        }
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
    public List<User> getFriends(Principal principal){

        User currentUser = getUserByPrincipal(principal);
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();

        /*
            suppose there are 3 users with id 1,2,3.
            if user1 add user2 as friend database record will be first user = user1 second user = user2
            if user3 add user2 as friend database record will be first user = user2 second user = user3
            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user
         */
        for (Friend friend : friendsByFirstUser) {
            friendUsers.add(userRepository.findUserById(friend.getSecondUser().getId()).orElse(null));
        }
        for (Friend friend : friendsBySecondUser) {
            friendUsers.add(userRepository.findUserById(friend.getFirstUser().getId()).orElse(null));
        }
        return friendUsers;

    }

    public Boolean checkToFriends(String friendId, Principal principal) {
        List<User> friends = getFriends(principal);
        return friends.stream().anyMatch((friend) -> friend.getId() == Long.parseLong(friendId));
    }
    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }


}
