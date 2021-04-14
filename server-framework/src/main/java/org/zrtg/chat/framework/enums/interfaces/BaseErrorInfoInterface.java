package org.zrtg.chat.framework.enums.interfaces;

/**
 * @author wangq
 * @create_at 2021-4-14 9:44
 */
public interface BaseErrorInfoInterface
{
    /**
     * 错误码
     * @return
     */
    String getResultCode();

    /**
     * 错误描述
     * @return
     */
    String getResultMsg();
}
