package com.example.Backuni.service;


import com.example.Backuni.dto.ResponseMessage;
import com.example.Backuni.entity.User;
import com.example.Backuni.exception.ResourceNotFoundException;
import com.example.Backuni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким email не существует!"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getAuthorities());
    }


    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("пользователь с таким email не существует!"));
        return user;
    }


    public ResponseMessage sendForgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("not found!")
        );
        if (user == null)
            return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "User email " + email + " not found!");

        LocalDateTime localDateTime = LocalDateTime.now();
        String message = "Hello, ! \n" +
                " Please, visit next link to change your password: http:https://hackathon-for-univer.herokuapp.com/user/changeForgotPassword/" + localDateTime;
        if (!mailService.send(user.getEmail(), "Change password", message))
            return new ResponseMessage(HttpStatus.BAD_GATEWAY.value(), "smtp server failure, request was not sent");
        return new ResponseMessage(HttpStatus.OK.value(), "Successfully sent");
    }
}
