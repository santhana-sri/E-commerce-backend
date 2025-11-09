package com.hex.ecommerce.controller;

import com.hex.ecommerce.dto.SignupRequestDto;
import com.hex.ecommerce.model.User;
import com.hex.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody SignupRequestDto signupRequest) {
        User user = userService.createUser(signupRequest);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User SignUp Success");
        response.put("username", user.getUsername());
        response.put("role", user.getRole().toString());
        
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @GetMapping("/login")
    public User login(Principal principal) {
        String username = principal.getName();
        User user = (User) userService.loadUserByUsername(username);
        return user;
    }

    // Test endpoints for different roles (like TRS project)
    @GetMapping("/public/hello")
    public String sayHello() {
        return "Hello Public!!!";
    }

    @GetMapping("/user/hello")
    public String sayHelloToUser(Principal principal) {
        String username = principal.getName();
        return "Hello Registered User--  " + username;
    }

    @GetMapping("/customer/hello")
    public String sayHelloToCustomer(Principal principal) {
        return "Hello Customer!! " + principal.getName();
    }

    @GetMapping("/vendor/hello")
    public String sayHelloToVendor(Principal principal) {
        return "Hello Vendor!! " + principal.getName();
    }

    @GetMapping("/admin/hello")
    public String sayHelloToAdmin(Principal principal) {
        return "Hello Admin!! " + principal.getName();
    }
}