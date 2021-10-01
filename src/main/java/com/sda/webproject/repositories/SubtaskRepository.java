package com.sda.webproject.repositories;

import com.sda.webproject.entities.SubtaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtaskRepository extends JpaRepository<SubtaskEntity, Integer> {
}
