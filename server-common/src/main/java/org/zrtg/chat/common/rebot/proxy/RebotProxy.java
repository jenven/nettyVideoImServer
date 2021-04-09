package org.zrtg.chat.common.rebot.proxy;


import org.zrtg.chat.common.model.MessageWrapper;

public  interface RebotProxy
{
	/**
	 * 机器人回复
	 * @param user  用于区分回复谁，机器人接口短暂记忆
	 * @param content
	 * @return
	 */
	 MessageWrapper botMessageReply(String user, String content);
}
