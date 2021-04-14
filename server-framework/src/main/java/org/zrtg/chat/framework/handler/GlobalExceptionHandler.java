package org.zrtg.chat.framework.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zrtg.chat.framework.enums.CommonEnum;
import org.zrtg.chat.framework.exception.BizException;
import org.zrtg.chat.framework.http.ResultBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangq
 * @create_at 2021-4-14 10:21
 */
@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultBody bizExceptionHandler(HttpServletRequest request, BizException e){
        logger.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return ResultBody.error(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 处理空指针异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest request,NullPointerException e){
        logger.error("发生空指针异常！原因是：{}",e.getMessage());
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理其他异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody  exceptionHandler(HttpServletRequest request,Exception e){
        logger.error("未知异常！原因是：{}",e.getMessage());
        return ResultBody.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }
}
