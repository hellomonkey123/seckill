package org.seckill.service;

import org.seckill.Exception.RepeatKillException;
import org.seckill.Exception.SeckillClosedException;
import org.seckill.Exception.SeckillException;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;

import java.util.List;

/**
 * @program: seckill.sql
 * @description: 秒杀商品接口,
 * @author: liu yuanwen
 * @create: 2019-06-12 15:18
 */

public interface SeckillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    public abstract List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    public abstract Seckill getById(long seckillId);

    /**
     * 开启秒杀接口
     * @param seckillId
     * @return
     */
    public abstract Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckill
     * @param userPhone
     * @param md5
     * @return
     */
    public abstract SeckillExecution executeSeckill(long seckill, long userPhone, String md5) throws SeckillClosedException, RepeatKillException, SeckillException;

    /**
     * 秒杀SQL存储过程
     * @param seckill
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillClosedException
     * @throws RepeatKillException
     * @throws SeckillException
     */
    public abstract SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);

}
