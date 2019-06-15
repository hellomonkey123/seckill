package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:spring-config.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception{
        long id = 1000L;
        long phone = 181880789211L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("insertCount: " + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1000L;
        long phone = 18168889211L;

        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}