package org.zrtg.chat.server.config;

import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.zrtg.chat.framework.group.RoomManager;
import org.zrtg.chat.framework.group.UserRegistry;
import org.zrtg.chat.server.handler.GroupVideoChatHandler;

/**
 * @author wangq
 * @create_at 2021-4-17 15:12
 */
@Configuration
public class GroupVideoChatHandlerConfig
{

    @Bean(name = "groupVideoChatHandler")
    public GroupVideoChatHandler groupVideoChatHandler() {
        return new GroupVideoChatHandler();
    }


}
