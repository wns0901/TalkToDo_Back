-- TalkToDo 테스트 쿼리
-- 1. user1의 모든 일정 조회
SELECT * FROM schedule WHERE user_id = 'user1';

-- 2. 2024년 7월의 일정 조회
SELECT * FROM schedule WHERE start_date BETWEEN '2024-07-01' AND '2024-07-31';

-- 3. 할일(TODO)만 조회
SELECT * FROM schedule WHERE is_todo = true;

-- 4. 캘린더에 표시되는 일정만 조회
SELECT * FROM schedule WHERE display_in_calendar = true;

USE talktodo_db; 