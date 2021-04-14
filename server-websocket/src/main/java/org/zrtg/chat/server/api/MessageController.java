package org.zrtg.chat.server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.zrtg.chat.common.domain.UserMessageEntity;
import org.zrtg.chat.framework.exception.BizException;

/**
 * @author wangq
 * @create_at 2021-4-14 10:38
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/message")
public class MessageController
{

    @GetMapping("/insert")
    public boolean insert(){

        log.info("开始抛出自定义异常");

        throw new BizException("201","消息体不能为空");

    }

    @GetMapping("/update")
    public boolean update(){
         log.info("开始抛出空指针异常");
         String content = null;
         content.equals("1111");
         return true;
    }

    @GetMapping("/delete")
    public boolean delete(){
        log.info("开始抛出异常");
        Integer.parseInt("abc123");
        return true;
    }

}
