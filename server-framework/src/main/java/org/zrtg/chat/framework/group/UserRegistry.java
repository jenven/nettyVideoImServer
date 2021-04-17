package org.zrtg.chat.framework.group;

import org.zrtg.chat.common.model.Session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangq
 * @create_at 2021-4-17 15:01
 */
public class UserRegistry
{
    private final ConcurrentHashMap<String, Session> usersBySessionId = new ConcurrentHashMap<>();

    public void register(Session user) {
        usersBySessionId.put(user.getAccount(), user);
    }

    public boolean exists(String sessionId) {
        return usersBySessionId.keySet().contains(sessionId);
    }

    public Session getBySessionId(String sessionId) {
        return usersBySessionId.get(sessionId);
    }

    public Session removeBySession(String sessionId) {
        final Session user = getBySessionId(sessionId);
        if (user !=null){
            usersBySessionId.remove(sessionId);
        }
        return user;
    }

}
