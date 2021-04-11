package org.zrtg.chat.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangq
 * @create_at 2021-4-11 13:45
 */
@Slf4j
@Component
public class SessionUtils
{


    static final int max = 1 << 16;

    static Map<String, String> sessions = new ConcurrentHashMap<>(max);

    /**
     * 10s清除一次
     */
    static final Long ONE_MINUTE = 10 * 1000L;

    /**
     * 这个记录了缓存使用的最后一次的记录，最近使用的在最前面
     */
    private static final List<String> CACHE_USE_LIST = new LinkedList<>();

    /**
     * 清理过期缓存是否在运行
     */
    private static volatile Boolean CLEAN_THREAD_IS_RUN = false;

    /**
     * 开启清理过期缓存的线程
     */
    static {
        if (!CLEAN_THREAD_IS_RUN) {
            System.out.println("clean thread run start");
            CleanTimeOutThread cleanTimeOutThread = new CleanTimeOutThread();
            Thread thread = new Thread(cleanTimeOutThread);
            // 设置为后台守护线程
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * 保存缓存的使用记录
     */
    private static synchronized void saveCacheUseLog(String cacheKey) {
        synchronized (CACHE_USE_LIST) {
            CACHE_USE_LIST.remove(cacheKey);
            CACHE_USE_LIST.add(0, cacheKey);
        }
    }

    /**
     * 删除某个缓存
     */
    public static void deleteCache(String cacheKey) {
        sessions.remove(cacheKey);
    }

    /**
     * 删除最近最久未使用的缓存
     */
    private static void deleteLRU() {
        while (CACHE_USE_LIST.size() >= max) {
            String cacheKey = null;
            synchronized (CACHE_USE_LIST) {
                cacheKey = CACHE_USE_LIST.remove(CACHE_USE_LIST.size() - 1);
            }
            if (cacheKey != null) {
                deleteCache(cacheKey);
            }
        }
    }

    /**
     * 设置清理线程的运行状态为正在运行
     */
    static void setCleanThreadRun() {
        CLEAN_THREAD_IS_RUN = true;
    }

    static class CleanTimeOutThread implements Runnable {

        @Override
        public void run() {
            setCleanThreadRun();
            while (true) {
                deleteLRU();
                try {
                    Thread.sleep(ONE_MINUTE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public synchronized static String getSessionId(){
        boolean isExist = true;
        String sessionid = "";
        do{

            String currentuser = UUID.randomUUID().toString().replaceAll("-", "");
            String oldValue = sessions.putIfAbsent(currentuser,"1");
            saveCacheUseLog(currentuser);
            if (oldValue ==null){
                sessionid = currentuser;
                isExist = false;
            }
        }while (isExist);
       return sessionid;
    }



}
