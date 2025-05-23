INSERT INTO users (id, name, email, department, position, username, role, password, is_active) VALUES
(1, '홍길동', 'user1@example.com', '개발팀', '사원', 'hong', 'USER', 'password1', true),
(2, '김철수', 'user2@example.com', '기획팀', '대리', 'kim', 'USER', 'password2', true),
(3, '이영희', 'user3@example.com', '디자인팀', '과장', 'lee', 'USER', 'password3', true),
(4, '박민수', 'user4@example.com', '개발팀', '부장', 'park', 'USER', 'password4', true),
(5, '최지우', 'user5@example.com', '마케팅팀', '사원', 'choi', 'USER', 'password5', true),
(6, '정우성', 'user6@example.com', '개발팀', '차장', 'jung', 'USER', 'password6', true),
(7, '한가인', 'user7@example.com', '기획팀', '사원', 'han', 'USER', 'password7', true),
(8, '신동엽', 'user8@example.com', '디자인팀', '대리', 'shin', 'USER', 'password8', true),
(9, '유재석', 'user9@example.com', '마케팅팀', '과장', 'yoo', 'USER', 'password9', true),
(10, '강호동', 'user10@example.com', '개발팀', '사원', 'kang', 'USER', 'password10', true);

INSERT INTO meetings (id, title, created_by, audio_url, transcript_id, favorite)
VALUES
(1, '주간 회의', 1, NULL, NULL, false),
(2, '기획 회의', 2, NULL, NULL, false),
(3, '디자인 회의', 3, NULL, NULL, false),
(4, '개발 회의', 4, NULL, NULL, false),
(5, '마케팅 회의', 5, NULL, NULL, false),
(6, '스터디', 6, NULL, NULL, false),
(7, '프로젝트 킥오프', 7, NULL, NULL, false),
(8, '회고', 8, NULL, NULL, false);

INSERT INTO schedules (
  user_id, title, start_date, end_date, category, type, completed, display_in_calendar, is_todo, original_todo_id, added_to_my_schedule, meeting_id
) VALUES
(1, '회사 회의', '2025-05-05', '2025-05-05', 'COMPANY', 'NORMAL', false, true, false, null, false, 1),
(1, '팀 스크럼', '2025-05-07', '2025-05-07', 'TEAM', 'NORMAL', false, true, false, null, false, 1),
(2, '개인 일정', '2025-05-10', '2025-05-10', 'PERSONAL', 'NORMAL', false, true, false, null, false, 2),
(2, '보고서 제출', '2025-05-17', '2025-05-19', 'COMPANY', 'NORMAL', false, true, false, null, false, 2),
(3, '프로젝트 마감', '2025-05-21', '2025-05-29', 'COMPANY', 'NORMAL', false, true, false, null, false, 3),
(4, '팀 회식', '2025-05-23', '2025-05-23', 'TEAM', 'NORMAL', false, true, false, null, false, 4),
(5, '개인 공부', '2025-05-26', '2025-05-29', 'PERSONAL', 'NORMAL', false, true, false, null, false, 5),
(6, 'TODO 일정1', '2025-05-26', '2025-05-29', 'TODO', 'TODO', false, true, true, 1, true, 6),
(7, 'TODO 일정2', '2025-05-27', '2025-05-28', 'TODO', 'TODO', false, true, true, 2, true, 7),
(8, 'TODO 일정3', '2025-05-28', '2025-05-29', 'TODO', 'TODO', true, true, true, 3, true, 8);

-- todos 테이블 컬럼 정리
ALTER TABLE todos DROP COLUMN description;
ALTER TABLE todos DROP COLUMN user_id;
ALTER TABLE todos DROP COLUMN created_date;
ALTER TABLE todos DROP COLUMN modified_date;
ALTER TABLE todos DROP COLUMN created_at;

ALTER TABLE todos ADD COLUMN is_schedule bit(1) NOT NULL DEFAULT false;

-- 엔티티에 맞는 INSERT문
INSERT INTO todos (meeting_id, title, assignee_id, due_date, status, added_to_my_todo, is_schedule) VALUES
(1, '보고서 제출', 1, '2025-05-17 18:00:00', 'PENDING', false, false),
(1, '자료 조사', 1, '2025-05-20 18:00:00', 'PENDING', false, false),
(2, '문서 정리', 2, '2025-05-15 18:00:00', 'COMPLETED', false, false),
(2, '이메일 확인', 2, '2025-05-16 18:00:00', 'PENDING', false, false),
(3, '코드 리뷰', 3, '2025-05-18 18:00:00', 'PENDING', false, false),
(4, '스터디 준비', 4, '2025-05-19 18:00:00', 'COMPLETED', false, false),
(5, '운동하기', 5, '2025-05-21 18:00:00', 'PENDING', false, false),
(6, '책 읽기', 6, '2025-05-22 18:00:00', 'PENDING', false, false),
(7, '회의록 정리', 7, '2025-05-23 18:00:00', 'COMPLETED', false, false),
(8, '프로젝트 계획', 8, '2025-05-24 18:00:00', 'PENDING', false, false);
