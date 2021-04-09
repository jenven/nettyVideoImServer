package org.zrtg.chat.framework.exception;

/**
 * @author wangq
 * @create_at 2021-4-8 14:23
 */
public class InitErrorException extends RuntimeException
{
    private static final long serialVersionUID = 4401440531171871948L;

    private int errorCode = 1;

    private String errorMsg;

    protected InitErrorException() {

    }

    public InitErrorException(String errorMsg, Throwable e) {
        super(errorMsg, e);
        this.errorMsg = errorMsg;
    }

    public InitErrorException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
