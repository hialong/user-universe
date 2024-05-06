-- auto-generated definition
create database user_center;

create table user
(
    id           bigint auto_increment comment 'User id'
        primary key,
    userName     varchar(256)                           null comment 'user name',
    userAccount  varchar(256)                           not null comment 'user account',
    avatarUrl    varchar(1024)                          null comment 'display avatar for user ',
    gender       tinyint      default 0                 null comment '0 -未知性别 1-男性 2-女性 9-武装直升机',
    userPassword varchar(512)                           not null comment 'password',
    phone        varchar(128)                           null comment 'subscriber identity',
    email        varchar(512)                           null comment 'email address',
    userStatus   int          default 0                 not null comment 'status 0 -normal 1-封号',
    updateTime   datetime     default CURRENT_TIMESTAMP null,
    createTime   datetime     default CURRENT_TIMESTAMP null comment 'the record create time',
    flag         tinyint      default 0                 not null comment 'isDelete',
    userRole     int          default 0                 null comment '用户角色，0-普通用户，1-管理员，9-超级管理员',
    specialCode  varchar(512) default '0'               null comment '特别编号，注册时使用，校验',
    score        bigint       default 0                 null comment '用户等级积分',
    extend_1     varchar(512)                           null comment '扩展字段',
    extend_2     varchar(512)                           null comment '扩展字段',
    extend_3     varchar(512)                           null comment '扩展字段'
)
    comment 'user table';

