DROP TABLE IF EXISTS `sys_user`;CREATE TABLE sys_user  (                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',                           `dis_name` varchar(20) NOT NULL COMMENT '显示名',                           `login_name` varchar(20) NOT NULL COMMENT '登录名',                           `password` varchar(64) NOT NULL COMMENT '密码',                           `delete_state` int Default 0 NOT NULL COMMENT '逻辑删除 0生效 1删除',                           `version` int Default 0 NOT NULL COMMENT '乐观锁',                           `tenant_id` int NOT NULL COMMENT '租户ID',                           PRIMARY KEY (`id`),                           INDEX `sys_user_login_name`(`login_name`) USING BTREE COMMENT '登录名') ENGINE=InnoDB  COMMENT='用户信息';insert into sys_user (dis_name, login_name, password, tenant_id) values ('周涛', 'allen', '1', 1);insert into sys_user (dis_name, login_name, password, tenant_id) values ('admin', 'admin', 'qaz123QAZ', 1);