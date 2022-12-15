package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.dto.UserDTO;
import com.volodymyrvasylyshyn.audi_server.facade.UserFacade;
import com.volodymyrvasylyshyn.audi_server.model.Interlocutor;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.InterlocutorRepository;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InterlocutorService {
    private final InterlocutorRepository interlocutorRepository;
    private final UserRepository userRepository;
    private final UserFacade userFacade;

    public InterlocutorService(InterlocutorRepository interlocutorRepository, UserRepository userRepository, UserFacade userFacade) {
        this.interlocutorRepository = interlocutorRepository;
        this.userRepository = userRepository;
        this.userFacade = userFacade;
    }

    public void addInterlocator(Principal principal, long secondUserId) {
        User currentUser = getUserByPrincipal(principal);
        UserDTO currentUserDTO = userFacade.userToUserDTO(currentUser);
        User user = userRepository.findUserById(secondUserId).orElse(null);
        UserDTO secondUserDTO = userFacade.userToUserDTO(user);
        Interlocutor interlocutor = new Interlocutor();
        User user1 = userRepository.findUserByUsername(currentUserDTO.getUsername()).orElse(null);
        User user2 = userRepository.findUserByUsername(secondUserDTO.getUsername()).orElse(null);
        User firstuser = user1;
        User seconduser = user2;
        if (!(interlocutorRepository.existsByFirstUserAndSecondUser(firstuser, seconduser))) {
            interlocutor.setCreatedDate(new Date());
            interlocutor.setFirstUser(firstuser);
            interlocutor.setSecondUser(seconduser);
            interlocutorRepository.save(interlocutor);
        }

    }
    public void addInterlocatorById(long firstUserId, long secondUserId) {
        User firstUser = userRepository.findUserById(firstUserId).orElse(null);
        UserDTO firstUserDTO = userFacade.userToUserDTO(firstUser);
        User secondUser = userRepository.findUserById(secondUserId).orElse(null);
        UserDTO secondUserDTO = userFacade.userToUserDTO(secondUser);
        Interlocutor interlocutor = new Interlocutor();
        User user1 = userRepository.findUserByUsername(firstUserDTO.getUsername()).orElse(null);
        User user2 = userRepository.findUserByUsername(secondUserDTO.getUsername()).orElse(null);
        User firstuser = user2;
        User seconduser = user1;
        if (!(interlocutorRepository.existsByFirstUserAndSecondUser(firstuser, seconduser))) {
            interlocutor.setCreatedDate(new Date());
            interlocutor.setFirstUser(firstuser);
            interlocutor.setSecondUser(seconduser);
            interlocutorRepository.save(interlocutor);
        }

    }

    public List<User> getInterlocutors(Principal principal) {

        User currentUser = getUserByPrincipal(principal);
        List<Interlocutor> interlocutorsByFirstUser = interlocutorRepository.findByFirstUser(currentUser);
//        List<Interlocutor> interlocutorsBySecondUser = interlocutorRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();
        for (Interlocutor interlocutor : interlocutorsByFirstUser) {
            friendUsers.add(userRepository.findUserById(interlocutor.getSecondUser().getId()).orElse(null));
        }
//        for (Interlocutor interlocutor : interlocutorsBySecondUser) {
//            friendUsers.add(userRepository.findUserById(interlocutor.getFirstUser().getId()).orElse(null));
//        }
        return friendUsers;

    }

    public void deleteInterlocator(Principal principal, long interlocatorForDeletedId) {
        User currentUser = getUserByPrincipal(principal);
        User secondUser = userRepository.findUserById(interlocatorForDeletedId).orElse(null);
        UserDTO secondUserDTO = userFacade.userToUserDTO(secondUser);
        if (!(interlocutorRepository.existsByFirstUserAndSecondUser(currentUser, secondUser))) {
            Interlocutor interlocutor = interlocutorRepository.findByFirstUserAndSecondUser(currentUser,secondUser);
            interlocutorRepository.delete(interlocutor);
        }
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }
}
