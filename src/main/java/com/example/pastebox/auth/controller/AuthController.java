package com.example.pastebox.auth.controller;


import com.example.pastebox.auth.dto.JwtRequest;
import com.example.pastebox.auth.dto.JwtResponse;
import com.example.pastebox.auth.dto.UserCreatedResponseDto;
import com.example.pastebox.auth.dto.UserRegisterDto;
import com.example.pastebox.auth.service.AuthService;
import com.example.pastebox.auth.service.UserService;
import com.example.pastebox.auth.util.error.response.AuthError;
import com.example.pastebox.auth.util.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthError("Неправильный логин или пароль"));
        }

        UserDetails userDetails = authService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registration")
    public UserCreatedResponseDto createNewUser(@RequestBody UserRegisterDto userRegisterDto){
        return authService.createNewUser(userRegisterDto);
    }
}
