package com.example.TalkToDo.repository;

import com.example.TalkToDo.entity.MeetingNote;
import com.example.TalkToDo.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeetingNoteRepository extends JpaRepository<MeetingNote, Long> {
    List<MeetingNote> findByMeeting(Meeting meeting);
} 