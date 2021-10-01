package com.sda.webproject.controllers;

import com.sda.webproject.entities.SubtaskEntity;
import com.sda.webproject.entities.TaskEntity;
import com.sda.webproject.entities.UserEntity;
import com.sda.webproject.repositories.FriendsRepository;
import com.sda.webproject.repositories.SubtaskRepository;
import com.sda.webproject.repositories.TaskRepository;
import com.sda.webproject.repositories.UserRepository;
import com.sda.webproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SecurityController securityController;


    @GetMapping("/subtasks/subTask-form/add/{taskId}")
    public ModelAndView addSubtask(@PathVariable Integer taskId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("subtask-form");
        SubtaskEntity subtaskEntity = new SubtaskEntity();
        subtaskEntity.setTaskId(taskId);
        modelAndView.addObject("subtask", subtaskEntity);
        return modelAndView;
    }

    @GetMapping("/subtasks/subTask-form/{subTaskId}")
    public ModelAndView editSubTask(@PathVariable(name = "subTaskId") Integer subTaskId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("subtask-form");
        modelAndView.addObject("subtask", subtaskRepository.findById(subTaskId).orElse(new SubtaskEntity()));
        return modelAndView;
    }

    @PostMapping("/subtasks/save")
    public ModelAndView saveSubTask(@ModelAttribute("subtask") SubtaskEntity subtaskEntity) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        TaskEntity taskEntity = taskRepository.getOne(subtaskEntity.getTaskId());
        subtaskEntity.setTask(taskEntity);
        subtaskRepository.save(subtaskEntity);
        return modelAndView;
    }

    @GetMapping("/subtasks/delete/{subTaskId}")
    public ModelAndView deleteSubtask(@PathVariable(name = "subTaskId") Integer subTaskId) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        subtaskRepository.deleteById(subTaskId);
        return modelAndView;
    }

    @GetMapping("/tasks/task-form1")
    public ModelAndView addTask() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("task-form1");
        TaskEntity taskEntity = new TaskEntity();
        checkLoggedInUser(taskEntity);
        modelAndView.addObject("task", taskEntity);
        return modelAndView;
    }

    @GetMapping("tasks/task-form1/{taskId}")
    public ModelAndView editTask(@PathVariable(name = "taskId") Integer taskId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("task-form1");
        modelAndView.addObject("task", taskRepository.findById(taskId).orElse(new TaskEntity()));
        return modelAndView;
    }

    @PostMapping("/tasks/save")
    public ModelAndView saveTask(@ModelAttribute("task") TaskEntity taskEntity) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        String categoryTask = taskEntity.getCategory();
        checkLoggedInUser(taskEntity);
        if (categoryTask.equals("A") || categoryTask.equals("B")) {
            taskEntity.setImportant(true);
        } else taskEntity.setImportant(false);

        taskRepository.save(taskEntity);
        return modelAndView;
    }

    @GetMapping("/tasks/delete/{taskId}")
    public ModelAndView deleteTask(@PathVariable(name = "taskId") Integer taskId) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        TaskEntity taskEntity = taskRepository.getOne(taskId);
        List<SubtaskEntity> subtaskEntities = taskEntity.getSubtasks();
        for (SubtaskEntity subtaskEntity : subtaskEntities) {
            subtaskRepository.delete(subtaskEntity);
        }
        taskRepository.deleteById(taskId);
        return modelAndView;
    }

    private void checkLoggedInUser(@ModelAttribute("task") TaskEntity taskEntity) {
        Optional<User> userEntity2 = securityController.getLoggedInUser();
        if (userEntity2.isPresent()) {
            System.out.println("userEntity2: " + userEntity2.get().getUsername());
            UserEntity userEntity = userRepository.findByUsername(userEntity2.get().getUsername());
            taskEntity.setUserId(userEntity.getUserId());
            taskEntity.setUser(userRepository.findById(taskEntity.getUserId()).orElse(new UserEntity()));
        }
    }


}
