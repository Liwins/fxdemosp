-- auto-generated definition
create table ums_admin
(
    id          bigint auto_increment
        primary key,
    username    varchar(64)      null,
    password    varchar(64)      null,
    icon        varchar(500)     null comment '头像',
    email       varchar(100)     null comment '邮箱',
    nick_name   varchar(200)     null comment '昵称',
    note        varchar(500)     null comment '备注信息',
    create_time datetime         null comment '创建时间',
    login_time  datetime         null comment '最后登录时间',
    status      int(1) default 1 null comment '帐号启用状态：0->禁用；1->启用'
)
    comment '后台用户表';
