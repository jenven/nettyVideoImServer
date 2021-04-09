package org.zrtg.chat.framework.exception;

/**
 * @author wangq
 * @create_at 2021-4-8 14:24
 */
public class PushException extends IllegalStateException
{
    private static final long serialVersionUID = -4953949710626671131L;

    public PushException() {
        super();
    }

    public PushException(String message) {
        super(message);
    }

    public PushException(Throwable throwable) {
        super(throwable);
    }

    public PushException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
