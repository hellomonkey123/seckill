--秒杀执行存储过程
DROP PROCEDURE IF EXISTS execute_seckill;
DELIMITER $$ --console;转换为 $$

--定义存储过程
--参数：in输入参数， out输出参数
--row_count() : 返回上一条修改类型SQL（delete、insert、update）的影响行数
--row_count： 0：未修改 ； >0 :表示修改的行数  <0 : 表示sql错误或者说未执行

--  下面的seckill.execute_seckill，seckill表示数据库名，execute_seckill表示存储过程名
create procedure seckill.execute_seckill
(in v_seckill_id bigint, in v_phone bigint,
 in v_kill_time timestamp ,out r_result int)
  begin
    declare insert_count int default 0;
    start transaction ;
    insert ignore into success_killed
      (seckill_id, user_phone, create_time)
      values (v_seckill_id, v_phone, v_kill_time);
    select row_count() into insert_count;
    if (insert_count = 0) then
      rollback ;
      set r_result = -1;
    elseif (insert_count < 0) then
      rollback ;
      set r_result = -2;
    else
      update seckill
      set number = number - 1
      where seckill_id = v_seckill_id
        and end_time > v_kill_time
        and start_time < v_kill_time
        and number > 0;
        select row_count() into insert_count;
        if (insert_count = 0) then
          rollback ;
          set r_result = 0;
        elseif (insert_count < 0) then
          rollback ;
          set r_result = -2;
        else
          commit;
          set r_result = 1;
        end if;
    end if;
  end
$$
--存储过程定义结束

delimiter ;

set @r_result=-3;
select * from seckill where seckill_id=1003\G
-- 执行存储过程
call execute_seckill(1003,13888888888,now(),@r_result);
-- 获取结果
select * from seckill where seckill_id=1003\G
select @r_result;

 show procedure status where db='seckill'; --显示seckill数据库下的存储过程的信息
 show create procedure seckill.execute_seckill; -- 显示seckill数据库下execute_seckill存储过程的详细信息
 alter procedure execute_seckill; --修改存储过程
 drop procedure execute_seckill; --删除叫execute_seckill的存储过程
 DROP PROCEDURE IF EXISTS execute_seckill; --或这样写

-- 存储过程
-- 1.存储过程优化：事务行级锁持有时间（缩短）
-- 2.不要过度依赖存储过程
-- 3.简单逻辑可以应用存储过程
-- 4.QPS：一个秒杀单6000/qps