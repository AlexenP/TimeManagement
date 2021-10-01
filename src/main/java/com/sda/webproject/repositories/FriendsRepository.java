package com.sda.webproject.repositories;

import com.sda.webproject.entities.FriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepository extends JpaRepository<FriendsEntity,Integer> {
}
