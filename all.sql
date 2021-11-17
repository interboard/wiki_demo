# 電子書表
drop table if exists `ebook`;
create table `ebook` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment '名稱',
  `category1_id` bigint comment '分類1',
  `category2_id` bigint comment '分類2',
  `description` varchar(200) comment '描述',
  `cover` varchar(200) comment '封面',
  `doc_count` int not null default 0 comment '文檔數',
  `view_count` int not null default 0 comment '閱讀數',
  `vote_count` int not null default 0 comment '點贊數',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='電子書';

insert into `ebook` (id, name, description) values (1, 'Spring Boot 入門教程', '零基礎入門 Java 開發，企業級應用開發最佳首選框架');
insert into `ebook` (id, name, description) values (2, 'Vue 入門教程', '零基礎入門 Vue 開發，企業級應用開發最佳首選框架');
insert into `ebook` (id, name, description) values (3, 'Python 入門教程', '零基礎入門 Python 開發，企業級應用開發最佳首選框架');
insert into `ebook` (id, name, description) values (4, 'Mysql 入門教程', '零基礎入門 Mysql 開發，企業級應用開發最佳首選框架');
insert into `ebook` (id, name, description) values (5, 'Oracle 入門教程', '零基礎入門 Oracle 開發，企業級應用開發最佳首選框架');

drop table if exists `test`;
create table `test` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment '名稱',
  `password` varchar(50) comment '密碼',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='測試';

insert into `test` (id, name, password) values (1, '測試', 'password');

# 分類
drop table if exists `category`;
create table `category` (
  `id` bigint not null comment 'id',
  `parent` bigint not null default 0 comment '父id',
  `name` varchar(50) not null comment '名稱',
  `sort` int comment '順序',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='分類';

insert into `category` (id, parent, name, sort) values (100, 000, '前端開發', 100);
insert into `category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `category` (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, '基礎應用', 201);
insert into `category` (id, parent, name, sort) values (202, 200, '框架應用', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort) values (301, 300, '基礎應用', 301);
insert into `category` (id, parent, name, sort) values (302, 300, '進階方向應用', 302);
insert into `category` (id, parent, name, sort) values (400, 000, '資料庫', 400);
insert into `category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `category` (id, parent, name, sort) values (500, 000, '其它', 500);
insert into `category` (id, parent, name, sort) values (501, 500, '伺服器', 501);
insert into `category` (id, parent, name, sort) values (502, 500, '開發工具', 502);
insert into `category` (id, parent, name, sort) values (503, 500, '熱門伺服器語言', 503);

-- 文檔表
drop table if exists `doc`;
create table `doc` (
  `id` bigint not null comment 'id',
  `ebook_id` bigint not null default 0 comment '電子書id',
  `parent` bigint not null default 0 comment '父id',
  `name` varchar(50) not null comment '名稱',
  `sort` int comment '順序',
  `view_count` int default 0 comment '閱讀數',
  `vote_count` int default 0 comment '點贊數',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文檔';

insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, '文檔1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, '文檔1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, '文檔2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, '文檔2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, '文檔2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, '文檔2.2.1', 1, 0, 0);

-- 文檔內容
drop table if exists `content`;
create table `content` (
  `id` bigint not null comment '文檔id',
  `content` mediumtext not null comment '內容',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文檔內容';

-- 用戶表
drop table if exists `user`;
create table `user` (
  `id` bigint not null comment 'ID',
  `login_name` varchar(50) not null comment '登入名',
  `name` varchar(50) comment '暱稱',
  `password` char(32) not null comment '密碼',
  primary key (`id`),
  unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='用戶';

insert into `user` (id, `login_name`, `name`, `password`) values (1, 'test', '測試', 'e70e2222a9d67c4f2eae107533359aa4');
-- 電子書快照表
drop table if exists `ebook_snapshot`;
create table `ebook_snapshot` (
  `id` bigint auto_increment not null comment 'id',
  `ebook_id` bigint not null default 0 comment '電子書id',
  `date` date not null comment '快照日期',
  `view_count` int not null default 0 comment '閱讀數',
  primary key (`id`),
  `vote_count` int not null default 0 comment '投票數'
  ) engine=innodb default charset=utf8mb4 comment='電子書快照表';