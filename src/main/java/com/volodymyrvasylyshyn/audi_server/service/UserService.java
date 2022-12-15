package com.volodymyrvasylyshyn.audi_server.service;


import com.volodymyrvasylyshyn.audi_server.dto.UserDTO;
import com.volodymyrvasylyshyn.audi_server.enums.ERole;
import com.volodymyrvasylyshyn.audi_server.exeptions.UserExistException;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.repository.UserRepository;
import com.volodymyrvasylyshyn.audi_server.request.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    public List<User> getAllUser() {
        return userRepository.findAllByOrderByCreatedDateDesc();
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.USER);
        user.setIsEnable(true);
        try {
            LOG.info("Saving User {}" + userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {

            LOG.error("Error during registration. {}" + e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }


    }


    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);

    }

    public User getCurrentUser(Principal principal) {

        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        User user =  userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
        System.out.println(user.getRoles());
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public User getUserById(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private void setNewResetPasswordToken(String token, String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }

    }

    public void forgotPassword(String email) {
//        User user1 = userRepository.findUserById(2L).orElse(null);
//        User user2 = userRepository.findUserById(4L).orElse(null);
//        User user3 = userRepository.findUserById(5L).orElse(null);
//        User user4 = userRepository.findUserById(6L).orElse(null);
//        User user5 = userRepository.findUserById(7L).orElse(null);
//        user1.setIsEnable(true);
//        user2.setIsEnable(true);
//        user3.setIsEnable(true);
//        user4.setIsEnable(true);
//        user5.setIsEnable(true);
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//        userRepository.save(user4);
//        userRepository.save(user5);
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null) {
            LOG.error("User with email:{} not found" + email);
            throw new UserExistException("User with email: "+email+" not found");
        } else {
            String resetPasswordToken = UUID.randomUUID().toString();
            setNewResetPasswordToken(resetPasswordToken, email);
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to AUDI. Please, visit next link: http://localhost:8080/forget/%s",
                    user.getUsername(),
                    resetPasswordToken
            );
            emailSenderService.sendMail(email, "FORGOT YOUR PASSWORD", message);
        }
    }

    public void updateResetPassword(String token, String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserExistException("User with email: " + email + " not found");
        }
    }

    public User getByResetToken(String resetPasswordToken) {
        return userRepository.findUserByResetPasswordToken(resetPasswordToken).orElseThrow(() -> new UserExistException("User with token: " + resetPasswordToken + " not found"));
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public void banUser(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setIsEnable(!user.getIsEnable());
        userRepository.save(user);
    }

    public boolean changePassword(String oldPassword, String newPassword, Principal principal) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = getUserByPrincipal(principal);
        System.out.println(passwordEncoder.matches(oldPassword,user.getPassword()));
        if(passwordEncoder.matches(oldPassword,user.getPassword())){
           String newEncodePassword = passwordEncoder.encode(newPassword);
           user.setPassword(newPassword);
           return true;
        }
        else{
            return false;
        }
    }
}
