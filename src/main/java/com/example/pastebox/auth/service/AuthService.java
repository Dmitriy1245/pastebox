package com.example.pastebox.auth.service;

import com.example.pastebox.auth.dto.UserCreatedResponseDto;
import com.example.pastebox.auth.dto.UserRegisterDto;
import com.example.pastebox.auth.entity.User;
import com.example.pastebox.auth.util.error.exception.EmailAlreadyExistsExcpetion;
import com.example.pastebox.auth.util.error.exception.UsernameAlreadyExistsExcpetion;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User {"+username+"} doesn't exists"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );

    }

    @Transactional
    public UserCreatedResponseDto createNewUser(UserRegisterDto userRegisterDto){
        if(userService.existsByUsername(userRegisterDto.getUsername()))
            throw new UsernameAlreadyExistsExcpetion("Username {"+userRegisterDto.getUsername()+"} already exists");
        if(userService.existsByEmail(userRegisterDto.getEmail()))
            throw new EmailAlreadyExistsExcpetion("Email {"+userRegisterDto.getEmail()+"} already exists");
        User user = userRegisterDtoToUser(userRegisterDto);
        return userToUserCreatedResponseDto(userService.save(user));

    }

    private User userRegisterDtoToUser(UserRegisterDto userRegisterDto){
        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        return user;
    }

    private UserCreatedResponseDto userToUserCreatedResponseDto(User user){
        UserCreatedResponseDto userCreatedResponseDto = new UserCreatedResponseDto();
        userCreatedResponseDto.setUsername(user.getUsername());
        return userCreatedResponseDto;
    }

}