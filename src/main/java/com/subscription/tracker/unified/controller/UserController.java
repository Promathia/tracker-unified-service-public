package com.subscription.tracker.unified.controller;

import com.subscription.tracker.unified.model.request.UserCreateBody;
import com.subscription.tracker.unified.model.request.UserUpdateBody;
import com.subscription.tracker.unified.model.response.SingleUserResponseBody;
import com.subscription.tracker.unified.model.response.UserForClubResponseBody;
import com.subscription.tracker.unified.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Integer>> createUser(@Valid @RequestBody UserCreateBody body) {
        Map<String, Integer> result = new HashMap<>();
        result.put("id", userService.createUser(body));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/club/{clubId}/users")
    public ResponseEntity<List<UserForClubResponseBody>> getUsersForClub(@PathVariable String clubId) {
        List<UserForClubResponseBody> result = userService.getUsersForClub(clubId);
        if (result == null || result.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/club/{clubId}/user/{userId}")
    public ResponseEntity<UserForClubResponseBody> getUserForClub(@PathVariable String clubId,
                                                                        @PathVariable String userId) {
        UserForClubResponseBody result = userService.getUserForClub(clubId, userId);
        if (result == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<SingleUserResponseBody> getUser(@PathVariable String userId) {
        SingleUserResponseBody user = userService.getSingleUserData(userId);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/club/{clubId}/users/{userId}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable String userId,
                                             @PathVariable String clubId,
                                             @RequestBody UserUpdateBody body) {
        if (!userService.isInitiatorAllowedToUpdateUser(body.getInitiatorId(), clubId)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        boolean isUpdateSuccess = userService.updateUserData(userId, body);
        if (!isUpdateSuccess) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            Map<String, String> result = new HashMap<>();
            result.put("result", "success");
            return ResponseEntity.ok(result);
        }
    }

}
