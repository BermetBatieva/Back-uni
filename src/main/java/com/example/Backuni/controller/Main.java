package com.example.Backuni.controller;

import com.example.Backuni.service.UserService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("admin")
public class Main {

    @GetMapping("get")
    public String getHello(){
        return "Hello!";
    }
}
