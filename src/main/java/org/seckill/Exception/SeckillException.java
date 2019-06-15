package org.seckill.Exception;

/**
 * @program: seckill.sql
 * @description: 所有秒杀异常
 * @author: liu yuanwen
 * @create: 2019-06-12 15:35
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
