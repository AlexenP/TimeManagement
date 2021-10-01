package com.sda.webproject.controllers;


import com.sda.webproject.entities.FriendsEntity;
import com.sda.webproject.entities.SubtaskEntity;
import com.sda.webproject.entities.TaskEntity;
import com.sda.webproject.entities.UserEntity;
import com.sda.webproject.repositories.UserRepository;

import com.sda.webproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskService taskService;

    @Autowired
    private SecurityController securityController;

    @GetMapping("/")
    public ModelAndView getIndex(@RequestParam(name = "flag", defaultValue = "") String flag) {
        ModelAndView modelAndView = new ModelAndView("index");

        // user fictiv
        UserEntity userEntity = null;//new UserEntity();


        Optional<User> userEntity2 = securityController.getLoggedInUser();
        if (userEntity2.isPresent()) {
            System.out.println("userEntity2: " + userEntity2.get().getUsername());
            userEntity = userRepository.findByUsername(userEntity2.get().getUsername());
            List<TaskEntity> allTasksOfUser = taskService.listAllTasksByUser(userEntity);
            modelAndView.addObject("allTasksOfUser", allTasksOfUser);
            modelAndView.addObject("subtasksById", taskService.listAllSubtasksByUser(userEntity));
            modelAndView.addObject("orderedTasks", taskService.taskOrganizer(allTasksOfUser, userEntity));
            modelAndView.addObject("userList", userRepository.findAll());
        } else {
            System.out.println("user not logged in");
        }


        return modelAndView;
    }

}
