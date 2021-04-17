package org.zrtg.chat.framework.config;

import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.zrtg.chat.framework.group.RoomManager;
import org.zrtg.chat.framework.group.UserRegistry;

/**
 * @author wangq
 * @create_at 2021-4-17 15:12
 */
@Component
public class GroupVideoChatConfig
{

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create();
    }

    @Bean
    public UserRegistry registry() {
        return new UserRegistry();
    }

    @Bean
    public RoomManager roomManager() {
        return new RoomManager();
    }


}
