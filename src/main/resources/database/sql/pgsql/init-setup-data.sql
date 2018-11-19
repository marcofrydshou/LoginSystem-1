INSERT INTO users ( id, address, email, enabled, name, password, token_date, username )
VALUES
(100, 'test address1', 'test1@test.dk', true, 'test user1', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', '2018-11-07:00:00:00', 'admin'), -- Pass is '123'
(200, 'test address2', 'test2@test.dk', true, 'test user2', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', '2018-11-07:00:00:00', 'user'),
(300, 'test address3', 'test3@test.dk', true, 'test user3', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', '2018-11-07:00:00:00', 'super'),
(400, 'test address4', 'test4@test.dk', true, 'test user4', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', '2018-11-07:00:00:00', 'test1'),
(500, 'test address5', 'test5@test.dk', true, 'test user5', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', '2018-11-07:00:00:00', 'test2');

INSERT INTO roles ( id, authority, user_id)
VALUES
(100, 'ADMIN',100), --
(200, 'USER',200),
(300, 'SUPERUSER',300);
