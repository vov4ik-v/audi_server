package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.dto.UserDTO;
import com.volodymyrvasylyshyn.audi_server.facade.UserFacade;
import com.volodymyrvasylyshyn.audi_server.model.Interlocutor;
import com.volodymyrvasylyshyn.audi_server.model.User;
import com.volodymyrvasylyshyn.audi_server.service.InterlocutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/interlocutor")
public class InterlocutorController {
    private  final InterlocutorService interlocutorService;
    private final UserFacade userFacade;

    public InterlocutorController(InterlocutorService interlocutorService, UserFacade userFacade) {
        this.interlocutorService = interlocutorService;
        this.userFacade = userFacade;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addInterlocutor(@RequestParam("interlocutorId")String interlocutorId, Principal principal){
        interlocutorService.addInterlocator(principal,Integer.parseInt(interlocutorId));
        return ResponseEntity.ok("Interlocutor added successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getInterlocutors(Principal principal){
        List<User> myInterlocutors = interlocutorService.getInterlocutors(principal);
        List<UserDTO> myInterlocutorsDTO = myInterlocutors.stream().map(userFacade::userToUserDTO).collect(Collectors.toList());
        return new ResponseEntity<>(myInterlocutorsDTO, HttpStatus.OK);
    }
    @PostMapping("/delete")
    public ResponseEntity<?> deleteInterlocutor(@RequestParam("interlocutorId")String interlocutorId, Principal principal){
        interlocutorService.deleteInterlocator(principal,Integer.parseInt(interlocutorId));
        return ResponseEntity.ok("Interlocutor deleted successfully");
    }
}
