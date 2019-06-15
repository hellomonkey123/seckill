package org.seckill.Exception;

/**
 * @program: seckill.sql
 * @description: 重复秒杀异常
 * @author: liu yuanwen
 * @create: 2019-06-12 15:31
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String msg) {
        super(msg);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
