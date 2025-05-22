package com.example.Login_Page.AuthController;


import com.example.Login_Page.DTO.AuthRequest;
import com.example.Login_Page.DTO.ChangePasswordRequest;
import com.example.Login_Page.DTO.RegisterRequest;
import com.example.Login_Page.Entity.JwtUtil;
import com.example.Login_Page.Entity.User;
import com.example.Login_Page.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getEmail());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null)
            return ResponseEntity.badRequest().body("Email already exists");
        if (!request.getPassword().equals(request.getConfirmPassword()))
            return ResponseEntity.badRequest().body("Passwords do not match");

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .age(request.getAge())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) return ResponseEntity.badRequest().body("User not found");
        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            return ResponseEntity.badRequest().body("Passwords do not match");

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password changed successfully");
    }
}
