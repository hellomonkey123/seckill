-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill.sql;
use seckill.sql;
-- 创建秒杀库存表

CREATE TABLE seckill(
seckill_id bigint NOT NULL auto_increment COMMENT '商品库存id',
name varchar(120) NOT NULL COMMENT '商品名称',
number int NOT NULL COMMENT '库存数量',
start_time timestamp NOT NULL COMMENT '秒杀开启时间',
end_time timestamp NOT NULL COMMENT '秒杀结束时间',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB auto_increment=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据

insert into
    seckill(name,number,start_time,end_time)
values
    ('1000秒杀iPhone7',10,'2018-5-10 00:00:00','2018-5-11 00:00:00'),
    ('1000秒杀iPhone6',40,'2018-5-10 00:00:00','2018-5-11 00:00:00'),
    ('600秒杀红米9',100,'2018-5-10 00:00:00','2018-5-11 00:00:00'),
    ('800秒杀荣耀7',50,'2018-5-10 00:00:00','2018-5-11 00:00:00');

-- 秒杀成功明细表
create table success_killed(
seckill_id bigint NOT NULL COMMENT '秒杀商品名',
user_phone bigint NOT NULL COMMENT '用户手机号',
stable tinyint NOT NULL DEFAULT -1 COMMENT '状态显示：-1：无效 0：成功 1：已付款',
create_time timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone),
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

--用户登陆认证相关的信息

-- 连接数据库控制台
mysql -uroot -p

-- 手写方便记录自己对数据的操作