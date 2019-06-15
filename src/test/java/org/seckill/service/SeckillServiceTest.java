package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.Exception.RepeatKillException;
import org.seckill.Exception.SeckillClosedException;
import org.seckill.Exception.SeckillException;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-config.xml"})
public class SeckillServiceTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill.sql={}", seckill);
    }

    //秒杀逻辑整合成一段代码
    @Test
    public void exportSeckillUrl() {
        long id = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long phone = 18758792347L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}", seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillClosedException e){
                logger.error(e.getMessage());
            }catch (SeckillException e){
                logger.error(e.getMessage());
            }
        }else {
            logger.warn("exposer={}" ,exposer);
        }
    }

    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1003;
        long phone = 13879030103L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);

        if (exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone,md5);
            logger.info(execution.getStateInfo());
        }
    }

    /*@Test
    public void executeSeckill() {
        long id = 1003L;
        long phone = 18756794647L;
        String md5 = "31319cb623c097012d4e932efe7d5ba0";

        SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,md5);
        logger.info("result={}", seckillExecution);
    }*/
}