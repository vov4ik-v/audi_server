package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.common.ApiResponse;
import com.volodymyrvasylyshyn.audi_server.model.Email;
import com.volodymyrvasylyshyn.audi_server.service.EmailSenderService;
import com.volodymyrvasylyshyn.audi_server.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/email")
@CrossOrigin
public class EmailController {
    private final EmailService emailService;

    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

    private final EmailSenderService emailSenderService;

    public EmailController(EmailService emailService, EmailSenderService emailSenderService) {
        this.emailService = emailService;
        this.emailSenderService = emailSenderService;
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

    @GetMapping("/getEmails")
    public ResponseEntity<List<Email>> getAll(){
        List<Email> emails = emailService.getAll();
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }
    @GetMapping("/sentAllEmails")
    public @ResponseBody ResponseEntity sendAllEmail() {
        List<String> emails = emailService.getAllEmails(emailService.getAll());
        try {
            emailSenderService.sendMailToManyPerson(emails,"WELCOME","This is a welcome email for all emails!!");
        } catch (MessagingException e) {
            LOG.error("Error while sending out email ..{}", (Object) e.getStackTrace());
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Please check your inbox",HttpStatus.OK);


    }

    @GetMapping("/sentEmail/{user-email}")
    public @ResponseBody ResponseEntity sendSimpleEmail(@PathVariable("user-email") String email) throws MessagingException {
        try {
            emailSenderService.sendMail(email,"WELCOME","This is a welcome email for you!!");
        }
        catch (MailException e){
            LOG.error("Error while sending out email ..{}", (Object) e.getStackTrace());
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Please check your inbox",HttpStatus.OK);


    }
}
