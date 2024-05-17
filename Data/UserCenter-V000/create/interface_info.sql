use user_center;
-- 接口信息
create table if not exists user_center.`interface_info`
(
    `id` bigint auto_increment not null comment '主键' primary key,
    `name` varchar(256) not null comment '用户名',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `requestParams` text null comment '请求参数',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `operationType` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `flag` tinyint default 0 not null comment '是否删除（0-未删除，1-已删除）'
    ) comment '接口信息';
