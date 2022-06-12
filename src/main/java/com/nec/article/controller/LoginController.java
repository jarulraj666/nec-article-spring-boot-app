package com.nec.article.controller;

import com.nec.article.model.UserVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestController
@RequestMapping("login")
public class LoginController {

    private final UserDetailsService userDetailService;


    public LoginController(UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping(path = "/{userName}")
    public UserVO login(
            @PathVariable("userName") String username
            ) {

        UserDetails userDetails;
        try {
            userDetails = userDetailService.loadUserByUsername(username);
            return UserVO.builder().userName(userDetails.getUsername()).roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).build();

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

    }
}
