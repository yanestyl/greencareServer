package com.yanestyl.greencare.services;

import com.yanestyl.greencare.entity.Request;
import com.yanestyl.greencare.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDetailsService userDetailsService();

    User createUserIfNotExists(String phoneNumber);

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByPhoneNumber(String phoneNumber);

    void updateUser(User user);

    List<Request> getRequestsByUserId(Long userId);
}
