package com.BE.service.impl;

import com.BE.exception.exceptions.BadRequestException;
import com.BE.model.entity.User;
import com.BE.model.request.UserDTO;
import com.BE.repository.UserRepository;
import com.BE.service.UserService;
import com.BE.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDTO updateUser(UserDTO user) {
        if (!user.getNewPassword().equals(user.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        User updateUser = accountUtils.getCurrentUser();
        updateUser.setFullName(user.getFullName());
        updateUser.setUsername(user.getUserName());
        updateUser.setPhone(user.getPhoneNumber());
        updateUser.setAddress(user.getAddress());
        updateUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
        User user1 =  userRepository.save(updateUser);
        return modelMapper.map(user1, UserDTO.class);
    }

}
