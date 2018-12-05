INSERT INTO users ( id, address, email, enabled, name, password, username )
VALUES
(100, 'test address1', 'testxu2018@gmail.com', true, 'test user1', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', 'admin'), -- Pass is '123'
(200, 'test address2', 'test2@test.dk', true, 'test user2', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', 'super'),
(300, 'test address3', 'test3@test.dk', true, 'test user3', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', 'user'),
(400, 'test address4', 'test4@test.dk', true, 'test user4', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', 'test1'),
(500, 'test address5', 'test5@test.dk', true, 'test user5', '$2a$10$grY5ADKN2.2yLv2ZW5F0MuHMZZy3vYYdgWLMAYAGDa3qoEK9FqhP6', 'test2');

INSERT INTO roles ( id, authority, created, modified )
VALUES
(100, 'ADMIN', '2016-02-03 00:00:00.0','2016-02-03 00:00:00.0'),
(200, 'SUPERUSER','2016-02-03 00:00:00.0','2016-02-03 00:00:00.0'),
(300, 'USER','2016-02-03 00:00:00.0','2016-02-03 00:00:00.0'),
(400, 'CHANGE_PASSWORD_PRIVILEGE','2016-02-03 00:00:00.0','2016-02-03 00:00:00.0');

INSERT INTO user_role (user_id, role_id)
VALUES
(100,100), -- Administrator has admin authority
(200,200), -- SUPERUSER
(300,300), -- USER
(100,400), -- Administrator has change password privilege
(200,400), -- SUERUSER has change password privilege
(300,400); -- USER has change password privilege
