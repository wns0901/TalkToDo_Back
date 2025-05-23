package com.example.TalkToDo.repository;

import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByCreatedBy(User user);
    List<Meeting> findByFavoriteTrue();
} 