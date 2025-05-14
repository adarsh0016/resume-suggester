package com.adarsh.resumed.controller;

import com.adarsh.resumed.DTO.AuthRequest;
import com.adarsh.resumed.DTO.Response;
import com.adarsh.resumed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class signupController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody AuthRequest request) {
        userService.register(request.getUsername(), request.getPassword());
        Response response = new Response("user created", "success");
        return ResponseEntity.ok(response);
    }
}
