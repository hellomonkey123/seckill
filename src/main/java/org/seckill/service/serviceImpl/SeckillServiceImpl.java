package org.seckill.service.serviceImpl;

import org.apache.commons.collections.MapUtils;
import org.seckill.Exception.RepeatKillException;
import org.seckill.Exception.SeckillClosedException;
import org.seckill.Exception.SeckillException;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: seckill
 * @description: 实现SeckillService接口
 * @author: liu yuanwen
 * @create: 2019-06-12 15:42
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisDao redisDao;

    @Resource
    private SeckillDao seckillDao;
    @Resource
    private SuccessKilledDao successKilledDao;

    //md5加密盐值
    private final String slat = "jhadiwuori-021983-209rj3098ij`98710928%$^%";

    /**
     * 查询所有秒杀商品列表
     * @return
     */
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    /**
     * 查询单个秒杀商品
     * @param seckillId
     * @return
     */
    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 秒杀接口暴露
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化，超时的基础上维护一致性
        //访问redis
        Seckill seckill = redisDao.getSerialization(seckillId);
        //找不到该商品
        if (seckill == null) {
            //看数据有没有
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null){
                return new Exposer(false, seckillId);
            } else {
                //数据库没有缓存也没有就存到redis中
                redisDao.putSerialization(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        Date nowTime = new Date();

        //秒杀结束
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //获取加密秒杀商品标识
        String md5 = getMd5(seckillId);
        //成功打开秒杀接口
        return new Exposer(true,md5,seckillId);
    }
    //秒杀商品加密方法
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillClosedException, RepeatKillException, SeckillException {

        if (md5 == null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }

        Date nowTime = new Date();
        try {
            //
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if (insertCount <= 0){
                //表示重复秒杀
                throw new RepeatKillException("seckill repeated");
            }else {
                //下面是要行级锁
                int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
                if (updateCount <= 0){
                    //表示该秒杀结束  rollback操作
                    throw new SeckillClosedException("seckill is closed");
                }else{
                    //获取该条秒杀信息  commit操作
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillClosedException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译异常转化为运行时异常
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5){

        if (md5 == null || !md5.equals(getMd5(seckillId))){
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        Date time = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone",userPhone);
        map.put("KillTime", time);
        map.put("result", null);
        //System.out.println("time: " +time);
        try {
            seckillDao.killByProcedure(map);
            System.out.println("map_result: " + map.get("result"));
            int result = MapUtils.getInteger(map, "result");
            System.out.println("result: " + result);
            if (result == 1){
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
            }else{
                return new SeckillExecution(seckillId, Objects.requireNonNull(SeckillStatEnum.stateOf(result)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

}
