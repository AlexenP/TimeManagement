package com.sda.webproject.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class SecurityController {
    @GetMapping("/login-form")
    public ModelAndView showFormLogin(){
        return new ModelAndView("login");
    }

    public Optional<User> getLoggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(null != auth && auth.getPrincipal() instanceof User) {
            User user = (User)auth.getPrincipal();
            System.out.println(user.getUsername());
            user.getAuthorities().forEach(a -> System.out.println(a.getAuthority()));
            return Optional.of(user);
        }
        return Optional.empty();
    }

}
