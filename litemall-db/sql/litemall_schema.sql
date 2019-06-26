drop database if exists pinpin;
drop user if exists 'pinpin'@'localhost';
-- 支持emoji：需要mysql数据库参数： character_set_server=utf8mb4
create database pinpin default character set utf8mb4 collate utf8mb4_unicode_ci;
use pinpin;
create user 'admin'@'localhost' identified by '123456';
grant all privileges on pinpin.* to 'pinpin'@'localhost';
flush privileges;
