package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * @program: seckill.sql
 * @description: 秒杀成功接口
 * @author: liu yuanwen
 * @create: 2019-06-11 20:02
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复(联合主键）
     * @param seckillId
     * @param userPhone
     * @return 返回插入行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
