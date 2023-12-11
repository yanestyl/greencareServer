package com.yanestyl.greencare.controllers;

import com.yanestyl.greencare.entity.Request;
import com.yanestyl.greencare.entity.User;
import com.yanestyl.greencare.services.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("{userId}/requests")
    public ResponseEntity<List<Request>> getUserRequests(@PathVariable Long userId) {
        List<Request> requests = userService.getRequestsByUserId(userId);
        return new ResponseEntity<> (requests, HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody Map<String, String> updates) {
        System.out.println("Received updates: " + updates);
        Optional<User> existingUser = userService.getUserById(userId);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();

            // Обновляем только те поля, которые предоставлены в updates
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case "name":
                        userToUpdate.setName(value);
                        break;
                    case "email":
                        userToUpdate.setEmail(value);
                        break;
                    case "phoneNumber":
                        userToUpdate.setPhoneNumber(value);
                        break;
                    // Добавьте другие поля, если необходимо
                }
            }

            // Обновляем пользователя в базе данных
            userService.updateUser(userToUpdate);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
