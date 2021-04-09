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

        SpringApplication.run(ChatServerApp.class, args);
    }
}
