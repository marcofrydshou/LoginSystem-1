INSERT INTO users ( id, address, email, enabled, name, password, token, token_date, username )
VALUES
(100, 'test address1', 'test1@test.dk', true, 'test user1', '123', '123', '2018-11-07:00:00:00', 'test'),
(200, 'test address2', 'test2@test.dk', true, 'test user2', '123', '123', '2018-11-07:00:00:00', 'test2'),
(300, 'test address3', 'test3@test.dk', true, 'test user3', '123', '123', '2018-11-07:00:00:00', 'test3'),
(400, 'test address4', 'test4@test.dk', true, 'test user4', '123', '123', '2018-11-07:00:00:00', 'test4'),
(500, 'test address5', 'test5@test.dk', true, 'test user5', '123', '123', '2018-11-07:00:00:00', 'test5');

INSERT INTO roles ( id, name)
VALUES
(100, 'ROLE_ADMIN'),
(200, 'ROLE_USER');

INSERT INTO users_roles (user_id, role_id)
VALUES
(100,100),
(200,200)
