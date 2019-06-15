package org.seckill.entity;

import java.util.Date;

public class SuccessKilled {

    private long seckillId;

    private long userPhone;

    private short state;

    private Date createTime;



    //多对一，一个秒杀商品可能会有多个成功记录
    private Seckill seckill;

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public short getState() {
        return state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public void setState(short state) {
        this.state = state;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }
    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
