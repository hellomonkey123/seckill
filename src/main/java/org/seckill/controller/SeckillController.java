package org.seckill.controller;

import org.seckill.Exception.RepeatKillException;
import org.seckill.Exception.SeckillClosedException;
import org.seckill.Exception.SeckillException;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @program: seckill.sql
 * @description: web的controller文件
 * @author: liu yuanwen
 * @create: 2019-06-13 09:53
 */
@Controller
@RequestMapping("seckill")
public class SeckillController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value  = "/list", method = RequestMethod.GET)
    public String list(Model model){
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);

        return "/list"; // /WEB-INF/jsp/"list".jsp
    }

    @RequestMapping(value  = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if (seckillId == null){
            return "redirect:/seckill/list"; //
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "/detail";
    }

    //ajax 返回json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody //封装成json
    public SeckillResult<Exposer> exposr(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true,exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<>(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(
            @PathVariable("seckillId")Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "killPhone",required = false)Long phone){
        //SeckillResult<SeckillExecution> result;
        if (phone == null){
            return new SeckillResult<>(false,"waiting login");
        }
        
        try {
            //以下两者二选一
            /*//使用存储的方式实现秒杀
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,phone,md5);*/
            //事务性完成秒杀
            SeckillExecution execution = seckillService.executeSeckill(seckillId,phone,md5);
            return new SeckillResult<>(true,execution);
        } catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(true,seckillExecution);
        } catch (SeckillClosedException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<>(true,seckillExecution);
        } catch (SeckillException e){
            logger.error(e.getMessage(),e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(true,seckillExecution);
        }
    }

    //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date time = new Date();
        return new SeckillResult<>(true, time.getTime());
    }

}
