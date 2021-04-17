package org.zrtg.chat.server;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.zrtg.chat.common.utils.SpringUtils;
import org.zrtg.chat.framework.config.properties.DruidProperties;

/**
 *  <pre>
 *      -Dkms.url=ws://192.168.10.10:58888/kurento
 *  </pre>
 *
 * @author wangq
 * @create_at 2021-4-8 15:53
 */
@Slf4j
@ComponentScan(basePackages = { "org.zrtg.chat" })
@MapperScan({"org.zrtg.chat.**.mapper"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ChatServerApp
{

    public static void main(String[] args) {
        System.setProperty("kms.url","ws://127.0.0.1:8888/kurento");
        SpringApplication.run(ChatServerApp.class, args);
    }
}
