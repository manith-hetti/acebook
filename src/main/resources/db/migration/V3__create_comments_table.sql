DROP TABLE IF EXISTS comments;

CREATE TABLE comments(
	id SERIAL PRIMARY KEY,
	content TEXT,
	post_id BIGSERIAL,
	user_id BIGSERIAL,
	CONSTRAINT fk_post_id FOREIGN KEY(post_id) REFERENCES posts(id),
	CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users(id)
);