package com.kharitonovalilya.event_registration_api.service;

import com.kharitonovalilya.event_registration_api.dto.request.CreateUserRequest;
import com.kharitonovalilya.event_registration_api.dto.response.UserResponse;
import com.kharitonovalilya.event_registration_api.entity.User;
import com.kharitonovalilya.event_registration_api.exception.ConflictException;
import com.kharitonovalilya.event_registration_api.exception.NotFoundException;
import com.kharitonovalilya.event_registration_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException("Email is already existed");
        }

        User user = User.create(request.getName(), request.getEmail());
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserResponse.from(user);
    }
}
