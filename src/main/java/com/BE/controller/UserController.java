package com.BE.controller;

import com.BE.model.request.TourRequestDTO;
import com.BE.model.request.UserDTO;
import com.BE.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@SecurityRequirement(name ="api")
public class UserController {
    @Autowired
    private UserService userService;
    @PutMapping
    public ResponseEntity update(@RequestBody UserDTO userDTO) {
        UserDTO user = userService.updateUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
