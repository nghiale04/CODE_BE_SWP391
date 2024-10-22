package com.BE.service;


import com.BE.enums.RoleEnum;
import com.BE.exception.exceptions.BadRequestException;

import com.BE.model.EmailDetail;
import com.BE.model.request.LoginGoogleRequest;
import com.BE.model.request.ResetPasswordRequest;
import com.BE.model.response.AuthenticationResponse;
import com.BE.model.entity.User;
import com.BE.model.request.AuthenticationRequest;
import com.BE.model.request.LoginRequestDTO;
import com.BE.repository.UserRepository;
import com.BE.utils.AccountUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTService jwtService;


    @Autowired
    AccountUtils accountUtils;

    @Autowired
    EmailService emailService;

    public User register(AuthenticationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleEnum.USER);
       try {
           return userRepository.save(user);
       }catch (DataIntegrityViolationException e){
           System.out.println(e.getMessage());
           throw new DataIntegrityViolationException("Duplicate UserName");
       }
    }

    public AuthenticationResponse authenticate(LoginRequestDTO request){
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new NullPointerException("Wrong Id Or Password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) throw new NullPointerException("Wrong Id Or Password");

        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .address(user.getAddress())
                .username(user.getUsername())
                .phone(user.getPhone())
                .fullName(user.getFullName())
                .role(user.getRole())
                .token(jwtService.generateToken(user))
                .build();
    }


    public AuthenticationResponse loginGoogle (LoginGoogleRequest loginGoogleRequest) {
        try{
            FirebaseToken decodeToken = FirebaseAuth.getInstance().verifyIdToken(loginGoogleRequest.getToken());
            String email = decodeToken.getEmail();
            User user = userRepository.findByEmail(email);
            if(user == null) {
                user = new User();
                user.setFullName(decodeToken.getName());
                user.setEmail(email);
                user.setUsername(email);
                user.setRole(RoleEnum.USER);
                user = userRepository.save(user);
            }

            return AuthenticationResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .token(jwtService.generateToken(user))
                    .build();
        } catch (FirebaseAuthException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public void forgotPasswordRequest(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new BadRequestException("Email Not Found");
        }
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(user.getEmail());
        emailDetail.setSubject("Reset password for account " + user.getEmail() + "!");
        emailDetail.setMsgBody("aaa");
        emailDetail.setButtonValue("Reset Password");
        emailDetail.setFullName(user.getFullName());
        emailDetail.setLink("http://localhost:5173?token=" + jwtService.generateToken(user));

        Runnable r = new Runnable() {
            @Override
            public void run() {
                emailService.sendMailTemplate(emailDetail);
            }

        };
        new Thread(r).start();

    }

    public User resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = accountUtils.getCurrentUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        return userRepository.save(user);
    }


    public String admin(){
        return "admin";
    }



}

