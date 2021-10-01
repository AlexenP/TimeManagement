package com.sda.webproject.controllers;

import com.sda.webproject.entities.AuthoritiesEntity;
import com.sda.webproject.entities.UserEntity;
import com.sda.webproject.repositories.AuthoritiesRepository;
import com.sda.webproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @GetMapping("/users/user-form")
    public ModelAndView addUser(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-form");
        modelAndView.addObject("user",new UserEntity());
        return modelAndView;
    }

    @GetMapping("users/user-form/{userId}")
    public ModelAndView editUser(@PathVariable(name = "userId") Integer userId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-form");
        modelAndView.addObject("user",userRepository.findById(userId).orElse(new UserEntity()));
        return modelAndView;
    }

    @PostMapping("/users/save")
    public ModelAndView saveUser(@ModelAttribute("user") UserEntity userEntity){
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        userEntity.setEnabled(true);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setWorkTimeHours(userEntity.getFinishHour()-userEntity.getStartHour());
        userEntity.setWorkTimeMiuntes(0);
        userRepository.save(userEntity);
        AuthoritiesEntity authoritiesEntity = new AuthoritiesEntity();
        authoritiesEntity.setUsername(userEntity.getUsername());
        authoritiesEntity.setAuthority("USER");
        authoritiesEntity.setUser(userEntity);
        authoritiesRepository.save(authoritiesEntity);
        return modelAndView;
    }

}
