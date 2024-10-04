insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 't@t.com', 'jupo13', 'Seoul', '00000-000000-000000', 'ACTIVE', 0);

insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (2, 't2@t.com', 'jupo14', 'Seoul', '00000-000000-000001', 'PENDING', 0);

insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (1, 'hello world', 1678530673958, 1678530673958, 1);
