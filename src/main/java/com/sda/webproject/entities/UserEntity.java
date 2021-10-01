package com.sda.webproject.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String firstName;
    private String lastName;
    private Integer startHour;
    private Integer finishHour;
    private Integer workTimeHours;
    private Integer workTimeMiuntes;
    private Integer workLevel; // 1=chill,2 sau 3=intense

    private String username;
    private String password;
    private Boolean enabled;

    @Column(insertable = false, updatable = false)
    private Integer userTypeId;


    @OneToMany(mappedBy = "user")
    private List<FriendsEntity> friends;

    @OneToMany(mappedBy = "user")
    private List<TaskEntity> tasks;

    @OneToMany(mappedBy = "user")
    private List<AuthoritiesEntity> authorities;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getFinishHour() {
        return finishHour;
    }

    public void setFinishHour(Integer finishHour) {
        this.finishHour = finishHour;
    }

    public Integer getWorkTimeHours() {
        return workTimeHours;
    }

    public void setWorkTimeHours(Integer workTimeHours) {
        this.workTimeHours = workTimeHours;
    }

    public Integer getWorkTimeMiuntes() {
        return workTimeMiuntes;
    }

    public void setWorkTimeMiuntes(Integer workTimeMiuntes) {
        this.workTimeMiuntes = workTimeMiuntes;
    }

    public Integer getWorkLevel() {
        return workLevel;
    }

    public void setWorkLevel(Integer workLevel) {
        this.workLevel = workLevel;
    }

    public List<FriendsEntity> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendsEntity> friends) {
        this.friends = friends;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<AuthoritiesEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthoritiesEntity> authorities) {
        this.authorities = authorities;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }
}
