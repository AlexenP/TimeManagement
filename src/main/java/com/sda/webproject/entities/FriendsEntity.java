package com.sda.webproject.entities;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class FriendsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer friendId;
    private String name;

    public FriendsEntity(Integer friendId, String name, UserEntity user) {
        this.friendId = friendId;
        this.name = name;
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    public FriendsEntity() {

    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
