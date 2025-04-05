package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin("*")
@RestController
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/users/check_email")
    public ResponseEntity<String> checkDuplicateEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Integer id = request.get("id").isEmpty() ? -1: Integer.parseInt(request.get("id"));
         if(userService.isEmailUnique(id,email))
             return ResponseEntity.ok("EXISTED");
        return ResponseEntity.ok("AVAILABLE");
    }
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.listAll();
    }
}
