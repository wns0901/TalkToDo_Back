-- TalkToDo 일정/할일 테이블 DDL
CREATE TABLE schedule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    display_in_calendar BOOLEAN NOT NULL,
    is_todo BOOLEAN NOT NULL,
    original_todo_id BIGINT
);

-- user_id 인덱스
CREATE INDEX idx_schedule_user_id ON schedule(user_id); 