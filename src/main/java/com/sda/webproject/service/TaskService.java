package com.sda.webproject.service;

import com.sda.webproject.entities.FriendsEntity;
import com.sda.webproject.entities.SubtaskEntity;
import com.sda.webproject.entities.TaskEntity;
import com.sda.webproject.entities.UserEntity;
import com.sda.webproject.repositories.FriendsRepository;
import com.sda.webproject.repositories.SubtaskRepository;
import com.sda.webproject.repositories.TaskRepository;
import com.sda.webproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private SubtaskRepository subtaskRepository;

    public List<TaskEntity> listAllTasks() {
        return taskRepository.findAll();
    }

    public List<SubtaskEntity> listAllSubtasks() {
        return subtaskRepository.findAll();
    }

    public List<TaskEntity> listAllTasksByUser(UserEntity userEntity) {
        return listAllTasks().stream()
                .filter(s -> s.getUser().getUserId().equals(userEntity.getUserId()))
                .collect(Collectors.toList());
    }

    public List<SubtaskEntity> listAllSubtasksByUser(UserEntity userEntity) {
        return listAllSubtasks().stream()
                .filter(s -> s.getTask().getUser().getUserId().equals(userEntity.getUserId()))
                .collect(Collectors.toList());

    }

    public Boolean isTime(TaskEntity task, UserEntity user) {
        int timeRequired = 0;
        switch (task.getCategory()) {
            case "A":
            case "B":
                timeRequired = 60;
                break;
            case "C":
            case "D":
                timeRequired = 45;
                break;
            case "E":
                timeRequired = 30;
                break;
        }
        Integer userTime = 0;
        userTime = user.getWorkTimeHours() * 60;
        userTime += user.getWorkTimeMiuntes();

        Integer brakeTime = 0;
        return timeRequired - brakeTime <= userTime;
    }

    public Integer getBreakMinutesForWorkingLevel(UserEntity user) {
        if (user.getWorkLevel() == 1)
            return 15;
        else if (user.getWorkLevel() == 2)
            return 10;
        else if (user.getWorkLevel() == 3)
            return 5;
        return 1;
    }

    public List<TaskEntity> taskOrganizer(List<TaskEntity> tasks, UserEntity user) {
        List<TaskEntity> organizedTasksList = tasks.stream()
                .filter(t -> t.getUser().getUserId().equals(user.getUserId()))
                .sorted(Comparator.comparing(TaskEntity::getCategory))
                .collect(Collectors.toList());
        Integer noSubCategory = 0;
        String category = "A";
        for (TaskEntity task : organizedTasksList) {
            if (task.getCategory().equals(category)) {
                noSubCategory++;
            } else {
                noSubCategory = 1;
                category = task.getCategory();
            }
            task.setSubCategory(noSubCategory);
        }
        Boolean time = true;
        Integer startingHour = user.getStartHour();
        Integer startingMinute = 0;

        for (int i = 0; i < organizedTasksList.size() && time; i++) {
            time = isTime(organizedTasksList.get(i), user);
            if (time) {

                Integer totalTimeOfTaskInMinutes = 0;
                organizedTasksList.get(i).setStartHour(startingHour);
                organizedTasksList.get(i).setStartMinute(startingMinute);
                if (organizedTasksList.get(i).getCategory().equals("A") || organizedTasksList.get(i).getCategory().equals("B")) {


                    totalTimeOfTaskInMinutes = 0;
                    Integer subtasksTimeInMinutes = 0;
                    for (SubtaskEntity subtasks : organizedTasksList.get(i).getSubtasks())
                        subtasksTimeInMinutes += subtasks.getTime();
                    totalTimeOfTaskInMinutes += subtasksTimeInMinutes;

                    Integer oneBreakTimeInMinutes = getBreakMinutesForWorkingLevel(organizedTasksList.get(i).getUser());
                    Integer allBrakeTime = oneBreakTimeInMinutes * organizedTasksList.get(i).getSubtasks().size();
                    totalTimeOfTaskInMinutes += allBrakeTime;
                } else if (organizedTasksList.get(i).getCategory().equals("C") || organizedTasksList.get(i).getCategory().equals("D")) {
                    totalTimeOfTaskInMinutes = 45; // 45 minute default cat dureaza task-ul
                } else if (organizedTasksList.get(i).getCategory().equals("E")) {
                    totalTimeOfTaskInMinutes = 30; // 30 minute default cat dureaza task-ul
                }
                // scadem din user.getWorkTime
                Integer endingHourInHours = totalTimeOfTaskInMinutes / 60;
                Integer endingHourInMinutes2 = totalTimeOfTaskInMinutes % 60;
                Integer brakeTime = getBreakMinutesForWorkingLevel(organizedTasksList.get(i).getUser());
                user.setWorkTimeHours(user.getWorkTimeHours() - endingHourInHours);

                // exemplu ora 6:00 - 2:30
                if (user.getWorkTimeMiuntes() - endingHourInMinutes2 - brakeTime < 0) {
                    user.setWorkTimeHours(user.getWorkTimeHours() - 1);
                    user.setWorkTimeMiuntes(user.getWorkTimeMiuntes() + 60);
                }
                user.setWorkTimeMiuntes(user.getWorkTimeMiuntes() - endingHourInMinutes2 - brakeTime);

                //minute in ora si minute
                totalTimeOfTaskInMinutes += (startingHour * 60) + startingMinute;
                Integer endingHour = totalTimeOfTaskInMinutes / 60;
                Integer endingMinute = totalTimeOfTaskInMinutes % 60;
                organizedTasksList.get(i).setEndHour(endingHour);
                organizedTasksList.get(i).setEndMinute(endingMinute);


                // starting time pentru urmatorul task, care incepe dupa pauza

                totalTimeOfTaskInMinutes += brakeTime;
                startingHour = totalTimeOfTaskInMinutes / 60;
                startingMinute = totalTimeOfTaskInMinutes % 60;
            }
        }
        return organizedTasksList;
    }
}
