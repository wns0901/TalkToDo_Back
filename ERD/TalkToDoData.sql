-- TalkToDo 샘플 데이터
INSERT INTO schedules (user_id, title, start_date, end_date, event_category, type, display_in_calendar, is_todo, original_todo_id)
VALUES
('user1', '월간 팀 회의', '2024-07-05', '2024-07-05', '회사', '회사', true, false, NULL),
('user1', '주간 업무 계획 작성', '2024-07-04', '2024-07-04', '개인', '개인', false, true, NULL),
('user2', '영어 스터디', '2024-07-08', '2024-07-08', '개인', '개인', true, false, NULL),
('user2', '이메일 정리하기', '2024-07-10', '2024-07-10', 'TODO', 'TODO', true, true, 3); 