-- DB console accessible at localhost:8082/h2-console
-- username: sa
-- password: password

CREATE TABLE standard_user (
    id INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    pwd VARCHAR(255) NOT NULL
);

INSERT INTO standard_user (email, pwd) VALUES
    ('user1@dieti.com', '123'),
    ('user2@dieti.com', '456'),
    ('user3@dieti.com', '789');
