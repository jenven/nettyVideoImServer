package org.zrtg.chat.framework.http;

import org.zrtg.chat.framework.enums.CommonEnum;
import org.zrtg.chat.framework.enums.interfaces.BaseErrorInfoInterface;

/**
 * @author wangq
 * @create_at 2021-4-14 10:08
 */
public class ResultBody
{
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private Object result;


    public ResultBody(){

    }

    public ResultBody(BaseErrorInfoInterface errorInfoInterface){
        this.code = errorInfoInterface.getResultCode();
        this.message = errorInfoInterface.getResultMsg();
    }

    public static ResultBody success(){
        return success(null);
    }

    public static ResultBody success(Object data){
        ResultBody resultBody = new ResultBody();
        resultBody.setCode(CommonEnum.SUCCESS.getResultCode());
        resultBody.setMessage(CommonEnum.SUCCESS.getResultMsg());
        resultBody.setResult(data);
        return  resultBody;
    }

    public static ResultBody error(BaseErrorInfoInterface errorInfoInterface){
        ResultBody resultBody = new ResultBody();
        resultBody.setCode(errorInfoInterface.getResultCode());
        resultBody.setMessage(errorInfoInterface.getResultMsg());
        resultBody.setResult(null);
        return  resultBody;
    }

    public static ResultBody error(String code,String message){
        ResultBody resultBody = new ResultBody();
        resultBody.setCode(code);
        resultBody.setMessage(message);
        resultBody.setResult(null);
        return  resultBody;
    }

    public static ResultBody error(String message){
        ResultBody resultBody = new ResultBody();
        resultBody.setCode("201");
        resultBody.setMessage(message);
        resultBody.setResult(null);
        return  resultBody;
    }


    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Object getResult()
    {
        return result;
    }

    public void setResult(Object result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "ResultBody{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
