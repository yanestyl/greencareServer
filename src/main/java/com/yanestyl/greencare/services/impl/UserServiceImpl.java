package com.yanestyl.greencare.services.impl;

import com.yanestyl.greencare.entity.Request;
import com.yanestyl.greencare.entity.User;
import com.yanestyl.greencare.repository.RequestRepository;
import com.yanestyl.greencare.repository.UserRepository;
import com.yanestyl.greencare.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByPhoneNumber(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public User createUserIfNotExists(String phoneNumber) {
        // Проверяем существование пользователя по номеру телефона
        Optional<User> existingUser = getUserByPhoneNumber(phoneNumber);

        if (existingUser.isPresent()) {
            // Если пользователь существует, возвращаем его
            return existingUser.get();
        } else {
            // Если пользователя нет, создаем нового с номером телефона и значениями по умолчанию для name и email
            User newUser = User.builder().phoneNumber(phoneNumber).build();
            return userRepository.save(newUser);
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<Request> getRequestsByUserId(Long userId) {
        return requestRepository.findByUserId(userId);
    }

    @Override
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

}
