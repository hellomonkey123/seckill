package org.seckill.Exception;

/**
 * @program: seckill.sql
 * @description: 秒杀关闭异常
 * @author: liu yuanwen
 * @create: 2019-06-12 15:34
 */
public class SeckillClosedException extends SeckillException{

    public SeckillClosedException(String message) {
        super(message);
    }

    public SeckillClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
