-- create.sql

-- users
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    age BIGINT CHECK(age>=0),
    gender VARCHAR(50)
);

-- game
CREATE TABLE game (
    game_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) UNIQUE, -- 테스트 위해 unique 추가
    category VARCHAR(200),
    game_price BIGINT CHECK(game_price>=0),
    release_date DATE
);


-- purchase: game table 이후 생성
CREATE TABLE purchase (
    purchase_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    game_id BIGINT,
    item_type VARCHAR(100),
    purchase_price BIGINT CHECK(purchase_price>=0),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (game_id) REFERENCES game(game_id)
);

-- play_history: user,game 이후 생성
CREATE TABLE play_history (
	play_history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT,
	game_id BIGINT,
	total_playtime BIGINT,
	user_score BIGINT,
	last_play DATETIME,
	FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (game_id) REFERENCES game(game_id)
);

-- review: user, game 이후 생성
CREATE TABLE review (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    game_id BIGINT,
    audiovisual_rating SMALLINT CHECK(audiovisual_rating BETWEEN 0 AND 5),
    immersion_rating SMALLINT CHECK(immersion_rating BETWEEN 0 AND 5),
    story_rating SMALLINT CHECK(story_rating BETWEEN 0 AND 5),
    total_rating SMALLINT CHECK(total_rating BETWEEN 0 AND 5),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (game_id) REFERENCES game(game_id)
);

