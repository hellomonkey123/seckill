package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
*@Description:
*@Param:
*@return:
*@Author: liuyuanwen
*@date:
*/
public interface SeckillDao {
    /**
     *减库存
     * @param seckillId
     * @param killTime
     * @return 如果返回行数>1, 表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     *通过id查询秒杀商品
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    //当有多个参数的时候必须用注解，不然mybatis不能区分参数
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 存储过程实现
     * @param params
     */
    void killByProcedure(Map<String, Object> params);
}
